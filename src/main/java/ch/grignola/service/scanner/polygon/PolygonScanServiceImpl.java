package ch.grignola.service.scanner.polygon;

import ch.grignola.service.scanner.bitquery.AbstractBitqueryScanService;

import javax.enterprise.context.ApplicationScoped;

import static ch.grignola.model.Network.POLYGON;

@ApplicationScoped
public class PolygonScanServiceImpl extends AbstractBitqueryScanService implements PolygonScanService {

    public PolygonScanServiceImpl() {
        super(POLYGON);
    }
}
