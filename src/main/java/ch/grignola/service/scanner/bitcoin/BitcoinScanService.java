package ch.grignola.service.scanner.bitcoin;

import ch.grignola.service.scanner.bitquery.BitqueryClient;
import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static ch.grignola.model.Allocation.LIQUID;
import static ch.grignola.model.Network.BITCOIN;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@ApplicationScoped
public class BitcoinScanService implements ScanService {

    private static final Logger LOG = Logger.getLogger(BitcoinScanService.class);

    @Inject
    BitqueryClient bitqueryClient;

    @Inject
    @CacheName("bitcoin-cache")
    Cache cache;

    @Override
    public boolean accept(String address) {
        return address.startsWith("bc") && address.length() == 42;
    }

    @Override
    public List<ScannerTokenBalance> getAddressBalance(String address) {
        LOG.infof("Getting balance for address %s", address);
        double balance = cache.get(address, x -> bitqueryClient.getBitcoinBalances(address)).await().indefinitely();
        if (balance == 0) {
            return emptyList();
        }

        return singletonList(new ScannerTokenBalance(BITCOIN, LIQUID, BigDecimal.valueOf(balance), "BTC"));
    }
}
