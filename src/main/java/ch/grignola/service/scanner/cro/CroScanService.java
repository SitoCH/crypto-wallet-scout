package ch.grignola.service.scanner.cro;

import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;

import java.util.List;

public interface CroScanService extends ScanService {
    List<ScannerTokenBalance> getAddressBalance(String address);
}
