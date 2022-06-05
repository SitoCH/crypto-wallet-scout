package ch.grignola.service.scanner.optimism;

import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;

import java.util.List;

public interface OptimismScanService extends ScanService {
    List<ScannerTokenBalance> getAddressBalance(String address);
}
