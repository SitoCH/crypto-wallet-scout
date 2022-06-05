package ch.grignola.service.scanner.cronos;

import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;

import java.util.List;

public interface CronosScanService extends ScanService {
    List<ScannerTokenBalance> getAddressBalance(String address);
}
