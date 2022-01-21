package ch.grignola.service.scanner.polygon;

import ch.grignola.service.scanner.bitquery.BitqueryClient;
import ch.grignola.service.scanner.bitquery.model.Balance;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static ch.grignola.model.Allocation.LIQUID;
import static ch.grignola.model.Network.POLYGON;

@ApplicationScoped
public class PolygonScanServiceImpl implements PolygonScanService {

    private static final Logger LOG = Logger.getLogger(PolygonScanServiceImpl.class);

    @Inject
    BitqueryClient bitqueryClient;

    @Override
    public boolean accept(String address) {
        return address.startsWith("0x") && address.length() == 42;
    }

    @Override
    public List<ScannerTokenBalance> getAddressBalance(String address) {
        return bitqueryClient.getRawBalance("matic", address).stream()
                .map(x -> toAddressBalance(address, x))
                .toList();
    }

    private ScannerTokenBalance toAddressBalance(String address, Balance balance) {
        LOG.infof("Token balance for address %s on %s based on event for symbol %s (%s): %s", address, POLYGON, balance.currency.symbol, balance.currency.address, balance.value);
        BigDecimal nativeValue = BigDecimal.valueOf(balance.value);
        return new ScannerTokenBalance(POLYGON, LIQUID, nativeValue, balance.currency.symbol);
    }
}
