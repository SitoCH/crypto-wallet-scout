package ch.grignola.service.scanner.solana;

import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;

import java.util.List;

public interface SolanaScanService extends ScanService {
    List<ScannerTokenBalance> getAddressBalance(String address);
}
