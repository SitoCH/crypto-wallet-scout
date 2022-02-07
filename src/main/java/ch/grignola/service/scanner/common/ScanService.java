package ch.grignola.service.scanner.common;

import ch.grignola.model.BannedContract;
import ch.grignola.model.Network;

import java.util.List;
import java.util.Map;

public interface ScanService {

    boolean accept(String address);

    List<ScannerTokenBalance> getAddressBalance(String address, Map<Network, List<BannedContract>> bannedContracts);
}
