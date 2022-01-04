package ch.grignola.service.scanner;

import ch.grignola.service.scanner.avalanche.AvalancheScanService;
import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.cro.CroScanService;
import ch.grignola.service.scanner.polygon.PolygonScanService;
import ch.grignola.service.scanner.terra.TerraScanService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;


@ApplicationScoped
public class AddressBalanceCheckerImpl implements AddressBalanceChecker {

    @Inject
    PolygonScanService polygonScanService;
    @Inject
    AvalancheScanService avalancheScanService;
    @Inject
    TerraScanService terraScanService;
    @Inject
    CroScanService croScanService;

    private List<ScanService> getScanServices() {
        return List.of(polygonScanService, avalancheScanService, terraScanService, croScanService);
    }

    @Override
    public AddressBalance getAddressBalance(String address) {
        return new AddressBalance(getScanServices().stream()
                .filter(x -> x.accept(address))
                .map(x -> x.getAddressBalance(address))
                .flatMap(Collection::stream)
                .toList());
    }
}
