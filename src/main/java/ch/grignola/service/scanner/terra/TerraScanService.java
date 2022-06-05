package ch.grignola.service.scanner.terra;

import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;

import java.util.List;

public interface TerraScanService extends ScanService {
    List<ScannerTokenBalance> getAddressBalance(String address);
}
