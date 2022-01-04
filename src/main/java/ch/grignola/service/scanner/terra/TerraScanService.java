package ch.grignola.service.scanner.terra;

import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.TokenBalance;

import java.util.List;

public interface TerraScanService extends ScanService {
    List<TokenBalance> getAddressBalance(String address);
}
