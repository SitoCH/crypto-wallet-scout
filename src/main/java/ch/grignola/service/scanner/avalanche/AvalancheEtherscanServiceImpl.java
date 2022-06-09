package ch.grignola.service.scanner.avalanche;

import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.etherscan.AbstractEtherscanScanService;
import ch.grignola.service.scanner.etherscan.model.EthereumTokenBalanceResult;
import ch.grignola.service.scanner.etherscan.model.EthereumTokenEventResult;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.micrometer.core.annotation.Timed;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

import static ch.grignola.model.Network.AVALANCHE;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;

@ApplicationScoped
public class AvalancheEtherscanServiceImpl extends AbstractEtherscanScanService implements AvalancheEtherscanService {

    @Inject
    @RestClient
    AvalancheScanRestClient restClient;

    @ConfigProperty(name = "snowtrace.api.key")
    String apiKey;

    public AvalancheEtherscanServiceImpl() {
        super(AVALANCHE, RateLimiterRegistry.of(RateLimiterConfig.custom()
                .timeoutDuration(ofSeconds(30))
                .limitRefreshPeriod(ofMillis(1500))
                .limitForPeriod(4)
                .build()).rateLimiter("AvalancheEtherscanService"));
    }

    @Override
    @Timed(value = "addressBalance", extraTags = {"network", "AVALANCHE"})
    public List<ScannerTokenBalance> getAddressBalance(String address) {
        return internalGetAddressBalance(address);
    }

    @Override
    protected EthereumTokenBalanceResult getTokenBalance(String address, String contractAddress) {
        rateLimiter.acquirePermission();
        return restClient.getTokenBalance(apiKey, "tokenbalance", address, contractAddress);
    }

    @Override
    protected List<EthereumTokenEventResult> getTokenEvents(String address) {
        rateLimiter.acquirePermission();
        return restClient.getTokenEvents(apiKey, "tokentx", address).result;
    }

    @Override
    protected NetworkTokenBalance getNetworkTokenBalance(String address) {
        rateLimiter.acquirePermission();
        EthereumTokenBalanceResult balance = restClient.getBalance(apiKey, "balance", address);
        return new NetworkTokenBalance(balance.result, "AVAX", 18);
    }
}
