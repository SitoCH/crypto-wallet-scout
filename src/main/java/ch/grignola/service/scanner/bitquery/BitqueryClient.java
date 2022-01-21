package ch.grignola.service.scanner.bitquery;

import ch.grignola.service.scanner.bitquery.model.Balance;

import java.util.List;

public interface BitqueryClient {

    List<Balance> getRawBalance(String network, String address);

}
