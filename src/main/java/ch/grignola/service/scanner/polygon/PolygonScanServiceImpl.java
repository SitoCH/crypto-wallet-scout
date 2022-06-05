package ch.grignola.service.scanner.polygon;

import ch.grignola.model.BannedContract;
import ch.grignola.model.Network;
import ch.grignola.service.scanner.bitquery.AbstractEthereumBitqueryScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import io.micrometer.core.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;

import static ch.grignola.model.Network.POLYGON;

@ApplicationScoped
public class PolygonScanServiceImpl extends AbstractEthereumBitqueryScanService implements PolygonScanService {

    public PolygonScanServiceImpl() {
        super(POLYGON);
    }

    @Override
    @Timed(value = "addressBalance", extraTags = {"network", "POLYGON"})
    public List<ScannerTokenBalance> getAddressBalance(String address, Map<Network, List<BannedContract>> bannedContracts) {
        return internalGetAddressBalance(address, bannedContracts);
    }
}
