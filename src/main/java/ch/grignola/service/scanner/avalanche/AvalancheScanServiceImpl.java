package ch.grignola.service.scanner.avalanche;

import ch.grignola.service.scanner.bitquery.AbstractBitqueryScanService;

import javax.enterprise.context.ApplicationScoped;

import static ch.grignola.model.Network.AVALANCHE;

@ApplicationScoped
public class AvalancheScanServiceImpl extends AbstractBitqueryScanService implements AvalancheScanService {

    public AvalancheScanServiceImpl() {
        super(AVALANCHE);
    }
}
