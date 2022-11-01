package ch.grignola.service.scanner.dogecoin;

import ch.grignola.service.scanner.bitquery.BitqueryClient;
import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.List;

import static ch.grignola.model.Allocation.LIQUID;
import static ch.grignola.model.Network.DOGECOIN;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Singleton
public class DogecoinScanService implements ScanService {

    private static final Logger LOG = Logger.getLogger(DogecoinScanService.class);

    @Inject
    BitqueryClient bitqueryClient;

    @Inject
    @CacheName("dogecoin-cache")
    Cache cache;

    @Override
    public boolean accept(String address) {
        return address.startsWith("D") && address.length() == 34;
    }

    @Override
    public List<ScannerTokenBalance> getAddressBalance(String address) {
        LOG.infof("Getting balance for address %s", address);
        double balance = cache.get(address, x -> bitqueryClient.getDogecoinBalances(address)).await().indefinitely();
        if (balance == 0) {
            return emptyList();
        }

        return singletonList(new ScannerTokenBalance(DOGECOIN, LIQUID, BigDecimal.valueOf(balance), "DOGE"));
    }
}
