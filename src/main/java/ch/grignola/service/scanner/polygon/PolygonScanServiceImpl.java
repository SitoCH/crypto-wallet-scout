package ch.grignola.service.scanner.polygon;

import ch.grignola.model.Network;
import ch.grignola.service.scanner.common.AbstractEthereumScanService;
import ch.grignola.service.scanner.common.EthereumTokenBalanceResult;
import ch.grignola.service.scanner.common.EthereumTokenEventResult;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class PolygonScanServiceImpl extends AbstractEthereumScanService implements PolygonScanService {

    @Inject
    @RestClient
    PolygonScanRestClient polygonScanRestClient;

    @ConfigProperty(name = "polygonscan.api.key")
    String apiKey;

    @Override
    protected Network getNetwork() {
        return Network.POLYGON;
    }

    @Override
    protected EthereumTokenBalanceResult getTokenBalance(String address, String contractAddress) {
        return polygonScanRestClient.getTokenBalance(apiKey, "tokenbalance", address, contractAddress);
    }

    @Override
    protected List<EthereumTokenEventResult> getTokenEvents(String address) {
        return polygonScanRestClient.getTokenEvents(apiKey, "tokentx", address).result;
    }

    @Override
    protected NetworkTokenBalance getNetworkTokenBalance(String address) {
        EthereumTokenBalanceResult balance = polygonScanRestClient.getBalance(apiKey, "balance", address);
        return new NetworkTokenBalance(balance.result, "MATIC", 18);
    }
}
