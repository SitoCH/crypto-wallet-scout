package ch.grignola.service.scanner.polygon;

import ch.grignola.service.scanner.bitquery.AbstractEthereumBitqueryScanService;

import javax.enterprise.context.ApplicationScoped;

import static ch.grignola.model.Network.POLYGON;

@ApplicationScoped
public class PolygonScanServiceImpl extends AbstractEthereumBitqueryScanService implements PolygonScanService {

    public PolygonScanServiceImpl() {
        super(POLYGON);
    }
}
