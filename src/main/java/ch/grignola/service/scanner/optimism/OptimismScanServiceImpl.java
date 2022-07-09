package ch.grignola.service.scanner.optimism;

import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.etherscan.AbstractEtherscanScanService;
import ch.grignola.service.scanner.etherscan.model.EthereumTokenBalanceResult;
import ch.grignola.service.scanner.etherscan.model.EthereumTokenEventResult;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.micrometer.core.annotation.Timed;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

import static ch.grignola.model.Network.OPTIMISM;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;

@Singleton
public class OptimismScanServiceImpl extends AbstractEtherscanScanService implements OptimismScanService {

    @Inject
    @RestClient
    OptimismScanRestClient restClient;

    @ConfigProperty(name = "optimistic.api.key")
    String apiKey;

    public OptimismScanServiceImpl() {
        super(OPTIMISM, RateLimiter.of("OptimismScanService", RateLimiterConfig.custom()
                .timeoutDuration(ofSeconds(15))
                .limitRefreshPeriod(ofMillis(500))
                .limitForPeriod(2)
                .build()));
    }

    @Override
    @Timed(value = "addressBalance", extraTags = {"network", "OPTIMISM"})
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
        return new NetworkTokenBalance(balance.result, "ETH", 18);
    }
}
