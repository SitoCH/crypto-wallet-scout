package ch.grignola.service.scanner.avalanche;

import ch.grignola.model.BannedContract;
import ch.grignola.model.Network;
import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;

import java.util.List;
import java.util.Map;

public interface AvalancheScanService extends ScanService {
    List<ScannerTokenBalance> getAddressBalance(String address, Map<Network, List<BannedContract>> bannedContracts);
}
