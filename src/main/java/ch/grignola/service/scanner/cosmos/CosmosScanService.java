package ch.grignola.service.scanner.cosmos;

import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;

import java.util.List;

public interface CosmosScanService extends ScanService {
    List<ScannerTokenBalance> getAddressBalance(String address);
}
