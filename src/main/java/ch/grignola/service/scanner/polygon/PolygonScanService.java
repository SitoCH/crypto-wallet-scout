package ch.grignola.service.scanner.polygon;

import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;

import java.util.List;

public interface PolygonScanService extends ScanService {
    List<ScannerTokenBalance> getAddressBalance(String address);
}
