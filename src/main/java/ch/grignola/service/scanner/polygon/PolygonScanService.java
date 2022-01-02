package ch.grignola.service.scanner.polygon;

import ch.grignola.service.scanner.model.TokenBalance;
import ch.grignola.service.scanner.common.ScanService;

import java.util.List;

public interface PolygonScanService extends ScanService {
    List<TokenBalance> getAddressBalance(String address);
}
