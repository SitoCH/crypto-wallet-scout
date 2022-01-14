package ch.grignola.service.scanner.common;

import java.util.List;

public class ScannerAddressBalance {
    private final List<ScannerTokenBalance> tokenBalances;

    public ScannerAddressBalance(List<ScannerTokenBalance> tokenBalances) {
        this.tokenBalances = tokenBalances;
    }

    public List<ScannerTokenBalance> getTokenBalances() {
        return tokenBalances;
    }
}
