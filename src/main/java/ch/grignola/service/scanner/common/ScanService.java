package ch.grignola.service.scanner.common;

import ch.grignola.service.scanner.TokenBalance;

import java.util.List;

public interface ScanService {

    boolean accept(String address);

    List<TokenBalance> getAddressBalance(String address);
}
