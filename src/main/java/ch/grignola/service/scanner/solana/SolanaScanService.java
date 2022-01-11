package ch.grignola.service.scanner.solana;

import ch.grignola.service.scanner.TokenBalance;
import ch.grignola.service.scanner.common.ScanService;

import java.util.List;

public interface SolanaScanService extends ScanService {
    List<TokenBalance> getAddressBalance(String address);
}
