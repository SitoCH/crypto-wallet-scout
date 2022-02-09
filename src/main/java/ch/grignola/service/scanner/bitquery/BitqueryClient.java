package ch.grignola.service.scanner.bitquery;

import ch.grignola.service.scanner.bitquery.model.BitqueryBalance;

import java.util.List;

public interface BitqueryClient {

    List<BitqueryBalance> getRawBalance(String network, String address);

}
