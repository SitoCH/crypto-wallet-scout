package ch.grignola.service.scanner.avalanche;

import ch.grignola.service.scanner.bitquery.AbstractEthereumBitqueryScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import io.micrometer.core.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

import static ch.grignola.model.Network.AVALANCHE;

@ApplicationScoped
public class AvalancheScanServiceImpl extends AbstractEthereumBitqueryScanService implements AvalancheScanService {

    public AvalancheScanServiceImpl() {
        super(AVALANCHE);
    }

    @Override
    @Timed(value = "addressBalance", extraTags = {"network", "AVALANCHE"})
    public List<ScannerTokenBalance> getAddressBalance(String address) {
        return internalGetAddressBalance(address);
    }
}
