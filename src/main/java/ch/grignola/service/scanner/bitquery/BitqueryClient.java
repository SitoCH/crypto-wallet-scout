package ch.grignola.service.scanner.bitquery;

import ch.grignola.service.scanner.bitquery.model.BitqueryEthereumBalance;

import java.util.List;

public interface BitqueryClient {

    double getBitcoinBalances(String address);

    List<BitqueryEthereumBalance> getEthereumBalances(String network, String address);

}
