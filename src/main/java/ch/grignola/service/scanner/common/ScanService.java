package ch.grignola.service.scanner.common;

import java.util.List;

public interface ScanService {

    boolean accept(String address);

    List<ScannerTokenBalance> getAddressBalance(String address);
}
