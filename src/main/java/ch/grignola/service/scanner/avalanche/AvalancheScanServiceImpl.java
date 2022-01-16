package ch.grignola.service.scanner.avalanche;

import ch.grignola.model.Network;
import ch.grignola.service.scanner.common.AbstractEthereumScanService;
import ch.grignola.service.scanner.common.EthereumTokenBalanceResult;
import ch.grignola.service.scanner.common.EthereumTokenEventResult;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class AvalancheScanServiceImpl extends AbstractEthereumScanService implements AvalancheScanService {

    @Inject
    @RestClient
    AvalancheScanRestClient avalancheScanRestClient;

    @ConfigProperty(name = "snowtrace.api.key")
    String apiKey;

    @Override
    protected Network getNetwork() {
        return Network.AVALANCHE;
    }

    @Override
    protected EthereumTokenBalanceResult getTokenBalance(String address, String contractAddress) {
        return avalancheScanRestClient.getTokenBalance(apiKey, "tokenbalance", address, contractAddress);
    }

    @Override
    protected List<EthereumTokenEventResult> getTokenEvents(String address) {
        return avalancheScanRestClient.getTokenEvents(apiKey, "tokentx", address).result;
    }

    @Override
    protected NetworkTokenBalance getNetworkTokenBalance(String address) {
        EthereumTokenBalanceResult balance = avalancheScanRestClient.getBalance(apiKey, "balance", address);
        return new NetworkTokenBalance(balance.result, "AVAX", 18);
    }
}
