package ch.grignola.service.scanner.polygon;

import ch.grignola.service.scanner.AddressBalance;

public interface PolygonScanService {
    AddressBalance getAddressBalance(String address);
}
