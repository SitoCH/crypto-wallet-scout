package ch.grignola.service.scanner.aave;

import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;

import java.util.List;

public interface AaveScanService extends ScanService {

    List<ScannerTokenBalance> getAddressBalance(String address);

}
