package ch.grignola.service.scanner.cro;

import ch.grignola.model.Allocation;
import ch.grignola.model.Network;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.cro.model.CroBalanceResult;
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
public class CroScanServiceImpl implements CroScanService {

    private static final Logger LOG = Logger.getLogger(CroScanServiceImpl.class);
    private static final String SYMBOL = "CRO";

    @Inject
    @RestClient
    CroRestClient croRestClient;

    @Override
    public boolean accept(String address) {
        return address.startsWith("cro") && address.length() == 42;
    }

    @Override
    public List<ScannerTokenBalance> getAddressBalance(String address) {
        List<ScannerTokenBalance> balances = new ArrayList<>();
        CroBalanceResult result = croRestClient.getBalance(address);

        BigDecimal liquidValue = result.result.balance.stream().map(x -> new BigDecimal(x.amount)).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (liquidValue.compareTo(ZERO) != 0) {
            balances.add(toTokenBalance(address, LIQUID, liquidValue));
        }

        BigDecimal stackedValue = result.result.bondedBalance.stream().map(x -> new BigDecimal(x.amount)).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (stackedValue.compareTo(ZERO) != 0) {
            balances.add(toTokenBalance(address, STACKED, stackedValue));
        }

        BigDecimal unclaimedRewardsValue = result.result.totalRewards.stream().map(x -> new BigDecimal(x.amount)).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (unclaimedRewardsValue.compareTo(ZERO) != 0) {
            balances.add(toTokenBalance(address, UNCLAIMED_REWARDS, unclaimedRewardsValue));
        }

        return balances;
    }

    private ScannerTokenBalance toTokenBalance(String address, Allocation allocation, BigDecimal value) {
        BigDecimal tokenDigits = new BigDecimal("100000000");
        BigDecimal nativeValue = value.divide(tokenDigits, MathContext.DECIMAL64);
        LOG.infof("Token balance for address %s on CRO: %s", address, nativeValue);
        return new ScannerTokenBalance(Network.CRO, allocation, nativeValue, SYMBOL);
    }
}
