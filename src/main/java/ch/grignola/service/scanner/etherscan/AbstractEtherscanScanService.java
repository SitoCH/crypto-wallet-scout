package ch.grignola.service.scanner.etherscan;

import ch.grignola.model.Network;
import ch.grignola.service.scanner.common.AbstractScanService;
import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.etherscan.model.EthereumTokenBalanceResult;
import ch.grignola.service.scanner.etherscan.model.EthereumTokenEventResult;
import ch.grignola.utils.DistinctByKey;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static ch.grignola.model.Allocation.LIQUID;
import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.rightPad;


public abstract class AbstractEtherscanScanService extends AbstractScanService implements ScanService {
    private static final Logger LOG = Logger.getLogger(AbstractEtherscanScanService.class);

    protected final RateLimiter rateLimiter;

    private final Network network;

    @Inject
    @CacheName("etherscan-cache")
    Cache cache;

    protected AbstractEtherscanScanService(Network network, RateLimiter rateLimiter) {
        this.network = network;
        this.rateLimiter = rateLimiter;
        this.rateLimiter.drainPermissions();
    }

    @Override
    public boolean accept(String address) {
        return address.startsWith("0x") && address.length() == 42;
    }

    private boolean filterBannedContracts(Set<String> bannedContracts, String address, EthereumTokenEventResult balance) {
        if (bannedContracts.contains(balance.contractAddress)) {
            LOG.infof("Found banned contract for address %s on %s: %s (%s)", address, network, balance.tokenSymbol, balance.contractAddress);
            return false;
        }
        return true;
    }

    protected List<ScannerTokenBalance> internalGetAddressBalance(String address) {
        return cache.get(network + "-" + address, x -> getBalancesFromEtherscan(address)).await().indefinitely();
    }

    private List<ScannerTokenBalance> getBalancesFromEtherscan(String address) {
        ContractStatus contractStatus = getContractStatus(network);
        Stream<ScannerTokenBalance> networkTokenBalance = Stream.of(getNetworkTokenBalanceAsTokenBalance(address));
        Stream<ScannerTokenBalance> tokenBalances = getTokenEvents(address).stream()
                .filter(new DistinctByKey<EthereumTokenEventResult>(x -> x.contractAddress)::filterByKey)
                .filter(x -> filterBannedContracts(contractStatus.bannedContracts(), address, x))
                .map(x -> {
                    checkContractVerificationStatus(contractStatus.allVerifiedContracts(), network, x.contractAddress);
                    return toAddressBalance(address, x, getTokenBalance(address, x.contractAddress));
                });
        return Stream.concat(networkTokenBalance, tokenBalances)
                .filter(x -> x.nativeValue().compareTo(BigDecimal.valueOf(0.01)) > 0)
                .toList();
    }

    private ScannerTokenBalance toAddressBalance(String address, EthereumTokenEventResult tokenEvent, EthereumTokenBalanceResult tokenBalance) {
        LOG.infof("Token balance for address %s on %s based on event for symbol %s (%s): %s", address, network, tokenEvent.tokenSymbol, tokenEvent.contractAddress, tokenBalance.result);
        BigDecimal nativeValue = new BigDecimal(tokenBalance.result).divide((new BigDecimal(rightPad("1", parseInt(tokenEvent.tokenDecimal) + 1, '0'))), MathContext.DECIMAL64);
        return new ScannerTokenBalance(network, LIQUID, nativeValue, tokenEvent.tokenSymbol);
    }

    protected abstract EthereumTokenBalanceResult getTokenBalance(String address, String contractAddress);

    protected abstract List<EthereumTokenEventResult> getTokenEvents(String address);

    private ScannerTokenBalance getNetworkTokenBalanceAsTokenBalance(String address) {
        NetworkTokenBalance balance = getNetworkTokenBalance(address);
        BigDecimal nativeValue = new BigDecimal(balance.nativeValue).divide(new BigDecimal(rightPad("1", balance.tokenDecimals + 1, '0')), MathContext.DECIMAL64);
        return new ScannerTokenBalance(network, LIQUID, nativeValue, balance.tokenSymbol);
    }

    protected abstract NetworkTokenBalance getNetworkTokenBalance(String address);

    public record NetworkTokenBalance(BigInteger nativeValue, String tokenSymbol, int tokenDecimals) {

    }
}
