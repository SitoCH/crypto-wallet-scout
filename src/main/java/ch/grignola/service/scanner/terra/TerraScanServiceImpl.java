package ch.grignola.service.scanner.terra;

import ch.grignola.model.Allocation;
import ch.grignola.model.BannedContract;
import ch.grignola.model.Network;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.terra.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.exception.ResteasyWebApplicationException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

import static ch.grignola.model.Allocation.*;

@ApplicationScoped
public class TerraScanServiceImpl implements TerraScanService {

    private static final Logger LOG = Logger.getLogger(TerraScanServiceImpl.class);

    private static final Map<String, String> CONTRACTS;

    static {
        CONTRACTS = new HashMap<>();
        CONTRACTS.put("ANC", "terra14z56l0fp2lsf86zy3hty2z47ezkhnthtr9yq76");
    }

    @Inject
    @RestClient
    TerraRestClient terraRestClient;

    @Override
    public boolean accept(String address) {
        return address.startsWith("terra") && address.length() == 44;
    }

    @Override
    public List<ScannerTokenBalance> getAddressBalance(String address, Map<Network, List<BannedContract>> bannedContracts) {

        List<ScannerTokenBalance> balances = new ArrayList<>();

        TerraBalancesResponse balancesResponse = terraRestClient.getBalances(address);
        if (balancesResponse.balances != null) {
            balances.addAll(balancesResponse.balances.stream()
                    .map(x -> toTokenBalance(address, LIQUID, new BigDecimal(x.amount), x.denom))
                    .filter(Objects::nonNull)
                    .toList());
        }

        TerraStackingResponse stackingResponse = terraRestClient.getStacking(address);
        if (stackingResponse.delegationResponses != null) {
            balances.addAll(stackingResponse.delegationResponses.stream()
                    .map(x -> toTokenBalance(address, STACKED, new BigDecimal(x.balance.amount), x.balance.denom))
                    .filter(Objects::nonNull)
                    .toList());
        }

        TerraRewardsResponse rewardsResponse = terraRestClient.getRewards(address);
        if (rewardsResponse.rewards != null) {
            balances.addAll(rewardsResponse.rewards.stream()
                    .filter(x -> x.reward != null).flatMap(x -> x.reward.stream())
                    .map(x -> toTokenBalance(address, UNCLAIMED_REWARDS, new BigDecimal(x.amount), x.denom))
                    .filter(Objects::nonNull)
                    .toList());
        }

        balances.addAll(CONTRACTS.entrySet().stream()
                .map(x -> getBalanceFromContract(address, x.getValue(), x.getKey()))
                .filter(Objects::nonNull)
                .toList());

        return balances;
    }

    private String getAddressAsBase64Request(String address) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return Base64.getEncoder().encodeToString(objectMapper.writeValueAsBytes(new TerraContractBalanceRequest(address)));
    }

    private ScannerTokenBalance getBalanceFromContract(String address, String contract, String symbol) {
        try {
            TerraContractBalanceResponse balance = terraRestClient.getContractBalance(contract, getAddressAsBase64Request(address));
            long amount = balance.queryResult.balance;
            if (amount == 0) {
                return null;
            }
            BigDecimal tokenDigits = new BigDecimal("1000000");
            BigDecimal nativeValue = new BigDecimal(amount).divide(tokenDigits, MathContext.DECIMAL64);
            LOG.infof("Token balance for address %s on Terra: %s %s", address, nativeValue, symbol);
            return new ScannerTokenBalance(Network.TERRA, LIQUID, nativeValue, symbol);
        } catch (JsonProcessingException e) {
            LOG.warnf("Unable to parse balance response for %s on contract %s", address, contract, e);
            return null;
        } catch (ResteasyWebApplicationException e) {
            LOG.warnf("Unable to request balance for %s on contract %s", address, contract, e);
            return null;
        }
    }

    private String getNativeSymbol(String symbol) {
        if (symbol.equalsIgnoreCase("uluna")) {
            return "LUNA";
        }

        if (symbol.equalsIgnoreCase("uusd")) {
            return "UST";
        }

        LOG.infof("Found token not currently supported: %s", symbol);
        return null;
    }

    private ScannerTokenBalance toTokenBalance(String address, Allocation allocation, BigDecimal amount, String symbol) {
        String nativeSymbol = getNativeSymbol(symbol);
        if (nativeSymbol == null) {
            return null;
        }
        BigDecimal tokenDigits = new BigDecimal("1000000");
        BigDecimal nativeValue = amount.divide(tokenDigits, MathContext.DECIMAL64);
        LOG.infof("Token balance for address %s on Terra: %s %s", address, nativeValue, nativeSymbol);
        return new ScannerTokenBalance(Network.TERRA, allocation, nativeValue, nativeSymbol);
    }
}
