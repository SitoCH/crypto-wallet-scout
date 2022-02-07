package ch.grignola.service.scanner.cronos;

import ch.grignola.model.Allocation;
import ch.grignola.model.BannedContract;
import ch.grignola.model.Network;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.cronos.model.CronosBalanceResult;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ch.grignola.model.Allocation.*;
import static java.math.BigDecimal.ZERO;

@ApplicationScoped
public class CronosScanServiceImpl implements CronosScanService {

    private static final Logger LOG = Logger.getLogger(CronosScanServiceImpl.class);
    private static final String SYMBOL = "CRO";

    @Inject
    @RestClient
    CronosRestClient cronosRestClient;

    @Override
    public boolean accept(String address) {
        return address.startsWith("cro") && address.length() == 42;
    }

    @Override
    public List<ScannerTokenBalance> getAddressBalance(String address, Map<Network, List<BannedContract>> bannedContracts) {
        List<ScannerTokenBalance> balances = new ArrayList<>();
        CronosBalanceResult result = cronosRestClient.getBalance(address);

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
        return new ScannerTokenBalance(Network.CRONOS, allocation, nativeValue, SYMBOL);
    }
}
