package ch.grignola.service.scanner.avalanche;

import ch.grignola.service.scanner.bitquery.AbstractEthereumBitqueryScanService;

import javax.enterprise.context.ApplicationScoped;

import static ch.grignola.model.Network.AVALANCHE;

@ApplicationScoped
public class AvalancheScanServiceImpl extends AbstractEthereumBitqueryScanService implements AvalancheScanService {

    public AvalancheScanServiceImpl() {
        super(AVALANCHE);
    }
}
