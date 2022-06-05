package ch.grignola.service.scanner.avalanche;

import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;

import java.util.List;

public interface AvalancheEtherscanService extends ScanService {
    List<ScannerTokenBalance> getAddressBalance(String address);
}
