package ch.grignola.service.scanner;

import ch.grignola.service.scanner.avalanche.AvalancheScanService;
import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.model.AddressBalance;
import ch.grignola.service.scanner.polygon.PolygonScanService;
import ch.grignola.service.scanner.terra.TerraScanService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@ApplicationScoped
public class AddressBalanceCheckerImpl implements AddressBalanceChecker {

    @Inject
    PolygonScanService polygonScanService;
    @Inject
    AvalancheScanService avalancheScanService;
    @Inject
    TerraScanService terraScanService;

    private List<ScanService> getScanServices() {
        return List.of(polygonScanService, avalancheScanService, terraScanService);
    }

    @Override
    public AddressBalance getAddressBalance(String address) {
        return new AddressBalance(getScanServices().stream()
                .filter(x -> x.accept(address))
                .map(x -> x.getAddressBalance(address))
                .flatMap(Collection::stream)
                .collect(toList()));
    }
}
