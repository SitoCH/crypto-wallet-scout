package ch.grignola.service.scanner.avalanche;

import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.etherscan.AbstractEtherscanScanService;
import ch.grignola.service.scanner.etherscan.model.EthereumTokenBalanceResult;
import ch.grignola.service.scanner.etherscan.model.EthereumTokenEventResult;
import io.github.bucket4j.Bucket;
import io.micrometer.core.annotation.Timed;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

import static ch.grignola.model.Network.AVALANCHE;
import static io.github.bucket4j.Bandwidth.classic;
import static io.github.bucket4j.Refill.intervally;
import static java.time.Duration.ofMillis;

@ApplicationScoped
public class AvalancheEtherscanServiceImpl extends AbstractEtherscanScanService implements AvalancheEtherscanService {

    @Inject
    @RestClient
    AvalancheScanRestClient restClient;

    @ConfigProperty(name = "snowtrace.api.key")
    String apiKey;

    public AvalancheEtherscanServiceImpl() {
        super(AVALANCHE, Bucket.builder()
                .addLimit(classic(4, intervally(4, ofMillis(1000))).withInitialTokens(0))
                .build().asBlocking());
    }

    @Override
    @Timed(value = "addressBalance", extraTags = {"network", "AVALANCHE"})
    public List<ScannerTokenBalance> getAddressBalance(String address) {
        return internalGetAddressBalance(address);
    }

    @Override
    protected EthereumTokenBalanceResult getTokenBalance(String address, String contractAddress) throws InterruptedException {
        bucket.consume(1);
        return restClient.getTokenBalance(apiKey, "tokenbalance", address, contractAddress);
    }

    @Override
    protected List<EthereumTokenEventResult> getTokenEvents(String address) throws InterruptedException {
        bucket.consume(1);
        return restClient.getTokenEvents(apiKey, "tokentx", address).result;
    }

    @Override
    protected NetworkTokenBalance getNetworkTokenBalance(String address) throws InterruptedException {
        bucket.consume(1);
        EthereumTokenBalanceResult balance = restClient.getBalance(apiKey, "balance", address);
        return new NetworkTokenBalance(balance.result, "AVAX", 18);
    }
}
