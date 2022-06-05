package ch.grignola.service.scanner.bitquery;

import ch.grignola.model.Network;
import ch.grignola.service.scanner.bitquery.model.BitqueryEthereumBalance;
import ch.grignola.service.scanner.common.AbstractScanService;
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

public abstract class AbstractEthereumBitqueryScanService extends AbstractScanService implements ScanService {

    private static final Logger LOG = Logger.getLogger(AbstractEthereumBitqueryScanService.class);

    private final Network network;
    @Inject
    BitqueryClient bitqueryClient;

    protected AbstractEthereumBitqueryScanService(Network network) {
        this.network = network;
    }

    @Override
    public boolean accept(String address) {
        return address.startsWith("0x") && address.length() == 42;
    }


    protected List<ScannerTokenBalance> internalGetAddressBalance(String address) {
        LOG.infof("Getting %s balance for address %s", network, address);
        ContractStatus contractStatus = getContractStatus(network);
        return bitqueryClient.getEthereumBalances(getBitqueryNetwork(network), address).stream()
                .filter(x -> filterBannedContracts(contractStatus.bannedContracts(), address, x))
                .map(x -> {
                    checkContractVerificationStatus(contractStatus.allVerifiedContracts(), network, x.currency.address);
                    return toAddressBalance(address, x);
                })
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

    private boolean filterBannedContracts(Set<String> bannedContracts, String address, BitqueryEthereumBalance balance) {
        if (bannedContracts.contains(balance.currency.address)) {
            LOG.infof("Found banned contract for address %s on %s: %s (%s)", address, network, balance.currency.symbol, balance.currency.address);
            return false;
        }
        return true;
    }

    private ScannerTokenBalance toAddressBalance(String address, BitqueryEthereumBalance balance) {
        LOG.infof("Token balance for address %s on %s based on event for symbol %s (%s): %s", address, network, balance.currency.symbol, balance.currency.address, balance.value);
        BigDecimal nativeValue = BigDecimal.valueOf(balance.value);
        return new ScannerTokenBalance(network, LIQUID, nativeValue, balance.currency.symbol);
    }
}
