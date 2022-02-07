package ch.grignola.service.scanner.bitquery;

import ch.grignola.model.BannedContract;
import ch.grignola.model.Network;
import ch.grignola.repository.BannedContractRepository;
import ch.grignola.service.scanner.bitquery.model.Balance;
import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.NotSupportedException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static ch.grignola.model.Allocation.LIQUID;
import static ch.grignola.model.Network.AVALANCHE;
import static ch.grignola.model.Network.POLYGON;
import static java.util.stream.Collectors.toUnmodifiableSet;

public abstract class AbstractBitqueryScanService implements ScanService {

    private static final Logger LOG = Logger.getLogger(AbstractBitqueryScanService.class);

    private final Network network;

    @Inject
    BannedContractRepository bannedContractRepository;

    @Inject
    BitqueryClient bitqueryClient;

    protected AbstractBitqueryScanService(Network network) {
        this.network = network;
    }

    @Override
    public boolean accept(String address) {
        return address.startsWith("0x") && address.length() == 42;
    }

    @Override
    public List<ScannerTokenBalance> getAddressBalance(String address) {
        LOG.infof("Getting %s balance for address %s", network, address);
        Set<String> bannedContracts = bannedContractRepository.findByNetwork(network).stream()
                .map(BannedContract::getContractId)
                .collect(toUnmodifiableSet());

        return bitqueryClient.getRawBalance(getBitqueryNetwork(network), address).stream()
                .filter(x -> filterBannedContracts(bannedContracts, address, x))
                .map(x -> toAddressBalance(address, x))
                .toList();
    }

    private String getBitqueryNetwork(Network network) {
        if (network == POLYGON) {
            return "matic";
        }

        if (network == AVALANCHE) {
            return "avalanche";
        }

        throw new NotSupportedException();
    }

    private boolean filterBannedContracts(Set<String> bannedContracts, String address, Balance balance) {
        if (bannedContracts.contains(balance.currency.address)) {
            LOG.infof("Found banned contract for address %s on %s: %s (%s)", address, network, balance.currency.symbol, balance.currency.address);
            return false;
        }
        return true;
    }

    private ScannerTokenBalance toAddressBalance(String address, Balance balance) {
        LOG.infof("Token balance for address %s on %s based on event for symbol %s (%s): %s", address, network, balance.currency.symbol, balance.currency.address, balance.value);
        BigDecimal nativeValue = BigDecimal.valueOf(balance.value);
        return new ScannerTokenBalance(network, LIQUID, nativeValue, balance.currency.symbol);
    }
}
