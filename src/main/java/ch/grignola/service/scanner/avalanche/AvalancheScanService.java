package ch.grignola.service.scanner.avalanche;

import ch.grignola.service.scanner.model.TokenBalance;
import ch.grignola.service.scanner.common.ScanService;

import java.util.List;

public interface AvalancheScanService extends ScanService {
    List<TokenBalance> getAddressBalance(String address);
}
