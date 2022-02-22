package ch.grignola.service.scanner.polkadot;

import ch.grignola.model.Allocation;
import ch.grignola.model.BannedContract;
import ch.grignola.model.Network;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.polkadot.common.PolkadotBalanceResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ch.grignola.model.Allocation.LIQUID;
import static ch.grignola.model.Allocation.STACKED;
import static ch.grignola.model.Network.POLKADOT;
import static java.math.BigDecimal.ZERO;

@ApplicationScoped
public class PolkadotScanServiceImpl implements PolkadotScanService {

    private static final Logger LOG = Logger.getLogger(PolkadotScanServiceImpl.class);

    @Inject
    @RestClient
    PolkadotRestClient polkadotRestClient;

    @Override
    public boolean accept(String address) {
        return address.length() == 48;
    }

    @Override
    public List<ScannerTokenBalance> getAddressBalance(String address, Map<Network, List<BannedContract>> bannedContracts) {
        List<ScannerTokenBalance> balances = new ArrayList<>();
        PolkadotBalanceResponse result = polkadotRestClient.getBalance(address, "Polkadot");

        BigDecimal liquidValue = new BigDecimal(Long.parseLong(result.data.balances.availableBalance.substring(2), 16));
        if (liquidValue.compareTo(ZERO) != 0) {
            balances.add(toTokenBalance(address, LIQUID, liquidValue));
        }

        BigDecimal reservedValue = new BigDecimal(Long.parseLong(result.data.balances.reservedBalance.substring(2), 16));
        if (reservedValue.compareTo(ZERO) != 0) {
            balances.add(toTokenBalance(address, STACKED, reservedValue));
        }

        return balances;
    }

    private ScannerTokenBalance toTokenBalance(String address, Allocation allocation, BigDecimal value) {
        BigDecimal tokenDigits = new BigDecimal("10000000000");
        BigDecimal nativeValue = value.divide(tokenDigits, MathContext.DECIMAL64);
        LOG.infof("Token balance for address %s on Polkadot: %s", address, nativeValue);
        return new ScannerTokenBalance(POLKADOT, allocation, nativeValue, "DOT");
    }
}
