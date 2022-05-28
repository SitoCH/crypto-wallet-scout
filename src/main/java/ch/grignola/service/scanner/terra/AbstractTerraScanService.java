package ch.grignola.service.scanner.terra;

import ch.grignola.model.Allocation;
import ch.grignola.model.BannedContract;
import ch.grignola.model.Network;
import ch.grignola.repository.TerraTokenContractRepository;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.terra.client.TerraCommonRestClient;
import ch.grignola.service.scanner.terra.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.cache.Cache;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.exception.ResteasyWebApplicationException;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

import static ch.grignola.model.Allocation.*;
import static org.apache.commons.lang3.StringUtils.rightPad;

public abstract class AbstractTerraScanService {

    private static final Logger LOG = Logger.getLogger(AbstractTerraScanService.class);

    @Inject
    TerraTokenContractRepository terraTokenContractRepository;

    protected abstract Cache getCache();

    protected abstract Network getNetwork();

    protected abstract TerraCommonRestClient getClient();

    public boolean accept(String address) {
        return address.startsWith("terra") && address.length() == 44;
    }

    @SuppressWarnings("java:S1172")
    public List<ScannerTokenBalance> getAddressBalance(String address, Map<Network, List<BannedContract>> bannedContracts) {
        return getCache().get(address, x -> getBalances(address)).await().indefinitely();
    }

    private List<ScannerTokenBalance> getBalances(String address) {
        List<ScannerTokenBalance> balances = new ArrayList<>();

        TerraBalancesResponse balancesResponse = getClient().getBalances(address);
        if (balancesResponse.balances != null) {
            balances.addAll(balancesResponse.balances.stream()
                    .map(x -> toTokenBalance(address, LIQUID, new BigDecimal(x.amount), x.denom))
                    .filter(Objects::nonNull)
                    .toList());
        }

        TerraUnbondingResponse unbondingResponse = getClient().getUnbonding(address);
        if (unbondingResponse.unbondingResponses != null) {
            balances.addAll(unbondingResponse.unbondingResponses.stream()
                    .filter(x -> x.entries != null)
                    .flatMap(x -> x.entries.stream())
                    .map(x -> toTokenBalance(address, STACKED, new BigDecimal(x.balance), "uluna"))
                    .filter(Objects::nonNull)
                    .toList());
        }

        TerraStackingResponse stackingResponse = getClient().getStacking(address);
        if (stackingResponse.delegationResponses != null) {
            balances.addAll(stackingResponse.delegationResponses.stream()
                    .map(x -> toTokenBalance(address, STACKED, new BigDecimal(x.balance.amount), x.balance.denom))
                    .filter(Objects::nonNull)
                    .toList());
        }

        TerraRewardsResponse rewardsResponse = getClient().getRewards(address);
        if (rewardsResponse.rewards != null) {
            balances.addAll(rewardsResponse.rewards.stream()
                    .filter(x -> x.reward != null).flatMap(x -> x.reward.stream())
                    .map(x -> toTokenBalance(address, UNCLAIMED_REWARDS, BigDecimal.valueOf(x.amount), x.denom))
                    .filter(Objects::nonNull)
                    .toList());
        }


        balances.addAll(terraTokenContractRepository.streamAll()
                .map(x -> getBalanceFromContract(address, x.getContractId(), x.getDecimals(), x.getSymbol()))
                .filter(Objects::nonNull)
                .toList());

        return balances;
    }

    private String getAddressAsBase64Request(String address) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return Base64.getEncoder().encodeToString(objectMapper.writeValueAsBytes(new TerraContractBalanceRequest(address)));
    }

    private ScannerTokenBalance getBalanceFromContract(String address, String contract, Long decimals, String symbol) {
        try {
            TerraContractBalanceResponse balance = getClient().getContractBalance(contract, getAddressAsBase64Request(address));
            long amount = balance.queryResult.balance;
            if (amount == 0) {
                return null;
            }
            BigDecimal tokenDigits = new BigDecimal(rightPad("1", (int) (decimals + 1), '0'));
            BigDecimal nativeValue = new BigDecimal(amount).divide(tokenDigits, MathContext.DECIMAL64);
            LOG.infof("Token balance for address %s on Terra: %s %s", address, nativeValue, symbol);
            return new ScannerTokenBalance(getNetwork(), LIQUID, nativeValue, symbol);
        } catch (JsonProcessingException e) {
            LOG.warnf("Unable to parse balance response for %s on contract %s", address, contract, e);
            return null;
        } catch (ResteasyWebApplicationException e) {
            LOG.warnf("Unable to request balance for %s on contract %s", address, contract, e);
            return null;
        }
    }

    protected abstract String getNativeSymbol(String symbol);

    private ScannerTokenBalance toTokenBalance(String address, Allocation allocation, BigDecimal amount, String symbol) {
        String nativeSymbol = getNativeSymbol(symbol);
        if (nativeSymbol == null) {
            return null;
        }
        BigDecimal tokenDigits = new BigDecimal("1000000");
        BigDecimal nativeValue = amount.divide(tokenDigits, MathContext.DECIMAL64);
        LOG.infof("Token balance for address %s on Terra: %s %s", address, nativeValue, nativeSymbol);
        return new ScannerTokenBalance(getNetwork(), allocation, nativeValue, nativeSymbol);
    }
}
