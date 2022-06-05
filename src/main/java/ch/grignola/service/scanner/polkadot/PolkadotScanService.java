package ch.grignola.service.scanner.polkadot;

import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;

import java.util.List;

public interface PolkadotScanService extends ScanService {
    List<ScannerTokenBalance> getAddressBalance(String address);
}
