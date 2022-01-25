package ch.grignola.service.scanner.cosmos;

import ch.grignola.model.Allocation;
import ch.grignola.model.Network;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import static ch.grignola.model.Allocation.*;
import static java.math.BigDecimal.ZERO;

@ApplicationScoped
public class CosmosScanServiceImpl implements CosmosScanService {

    private static final Logger LOG = Logger.getLogger(CosmosScanServiceImpl.class);
    private static final String SYMBOL = "ATOM";

    @Inject
    @RestClient
    CosmosRestClient cosmosRestClient;

    @Override
    public boolean accept(String address) {
        return address.startsWith("cosmos") && address.length() == 45;
    }

    @Override
    public List<ScannerTokenBalance> getAddressBalance(String address) {

        List<ScannerTokenBalance> balances = new ArrayList<>();

        BigDecimal liquidValue = cosmosRestClient.getBalance(address).result.stream().map(x -> new BigDecimal(x.amount)).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (liquidValue.compareTo(ZERO) != 0) {
            balances.add(toTokenBalance(address, LIQUID, liquidValue));
        }

        BigDecimal stackedValue = cosmosRestClient.getStackedBalance(address).result.stream().map(x -> new BigDecimal(x.balance.amount)).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (stackedValue.compareTo(ZERO) != 0) {
            balances.add(toTokenBalance(address, STACKED, stackedValue));
        }

        BigDecimal unclaimedRewardsValue = cosmosRestClient.getRewardsBalance(address).result.total.stream().map(x -> new BigDecimal(x.amount)).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (unclaimedRewardsValue.compareTo(ZERO) != 0) {
            balances.add(toTokenBalance(address, UNCLAIMED_REWARDS, unclaimedRewardsValue));
        }

        return balances;
    }

    private ScannerTokenBalance toTokenBalance(String address, Allocation allocation, BigDecimal value) {
        BigDecimal tokenDigits = new BigDecimal("1000000");
        BigDecimal nativeValue = value.divide(tokenDigits, MathContext.DECIMAL64);
        LOG.infof("Token balance for address %s on ATOM: %s", address, nativeValue);
        return new ScannerTokenBalance(Network.TERRA, allocation, nativeValue, SYMBOL);
    }
}
