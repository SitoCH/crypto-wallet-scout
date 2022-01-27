package ch.grignola.service.scanner.polygon;

import ch.grignola.model.BannedContract;
import ch.grignola.repository.BannedContractRepository;
import ch.grignola.service.scanner.bitquery.BitqueryClient;
import ch.grignola.service.scanner.bitquery.model.Balance;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static ch.grignola.model.Allocation.LIQUID;
import static ch.grignola.model.Network.POLYGON;
import static java.util.stream.Collectors.toUnmodifiableSet;

@ApplicationScoped
public class PolygonScanServiceImpl implements PolygonScanService {

    private static final Logger LOG = Logger.getLogger(PolygonScanServiceImpl.class);

    @Inject
    BannedContractRepository bannedContractRepository;

    @Inject
    BitqueryClient bitqueryClient;

    @Override
    public boolean accept(String address) {
        return address.startsWith("0x") && address.length() == 42;
    }

    @Override
    public List<ScannerTokenBalance> getAddressBalance(String address) {

        Set<String> bannedContracts = bannedContractRepository.findByNetwork(POLYGON).stream()
                .map(BannedContract::getContractId)
                .collect(toUnmodifiableSet());

        return bitqueryClient.getRawBalance("matic", address).stream()
                .filter(x -> filterBannedContracts(bannedContracts, address, x))
                .map(x -> toAddressBalance(address, x))
                .toList();
    }

    private boolean filterBannedContracts(Set<String> bannedContracts, String address, Balance balance) {

        if (bannedContracts.contains(balance.currency.address)) {
            LOG.infof("Found banned contract for address %s on %s: %s (%s)", address, POLYGON, balance.currency.symbol, balance.currency.address);
            return false;
        }
        return true;
    }

    private ScannerTokenBalance toAddressBalance(String address, Balance balance) {
        LOG.infof("Token balance for address %s on %s based on event for symbol %s (%s): %s", address, POLYGON, balance.currency.symbol, balance.currency.address, balance.value);
        BigDecimal nativeValue = BigDecimal.valueOf(balance.value);
        return new ScannerTokenBalance(POLYGON, LIQUID, nativeValue, balance.currency.symbol);
    }
}
