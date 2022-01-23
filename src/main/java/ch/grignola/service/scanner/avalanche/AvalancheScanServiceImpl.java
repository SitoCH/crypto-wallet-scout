package ch.grignola.service.scanner.avalanche;

import ch.grignola.model.Network;
import ch.grignola.service.scanner.ethereum.AbstractEthereumScanService;
import ch.grignola.service.scanner.ethereum.EthereumTokenBalanceResult;
import ch.grignola.service.scanner.ethereum.EthereumTokenEventResult;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
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
    protected EthereumTokenBalanceResult getTokenBalance(String address, String contractAddress) throws InterruptedException {
        bucket.consume(1);
        return avalancheScanRestClient.getTokenBalance(apiKey, "tokenbalance", address, contractAddress);
    }

    @Override
    protected List<EthereumTokenEventResult> getTokenEvents(String address) throws InterruptedException {
        bucket.consume(1);
        return avalancheScanRestClient.getTokenEvents(apiKey, "tokentx", address).result;
    }

    @Override
    protected NetworkTokenBalance getNetworkTokenBalance(String address) throws InterruptedException {
        bucket.consume(1);
        EthereumTokenBalanceResult balance = avalancheScanRestClient.getBalance(apiKey, "balance", address);
        return new NetworkTokenBalance(balance.result, "AVAX", 18);
    }
}
