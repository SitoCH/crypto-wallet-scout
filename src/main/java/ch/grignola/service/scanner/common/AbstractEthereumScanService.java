package ch.grignola.service.scanner.common;

import ch.grignola.model.Network;
import ch.grignola.service.token.TokenPriceProvider;
import ch.grignola.service.scanner.TokenBalance;
import ch.grignola.utils.DistinctByKey;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BlockingBucket;
import io.github.bucket4j.Bucket;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.List;
import java.util.stream.Stream;

import static ch.grignola.model.Allocation.LIQUID;
import static java.lang.Integer.parseInt;
import static java.math.BigDecimal.ZERO;
import static java.time.Duration.ofMillis;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.rightPad;

public abstract class AbstractEthereumScanService implements ScanService {

    private static final Logger LOG = Logger.getLogger(AbstractEthereumScanService.class);

    @Inject
    protected TokenPriceProvider tokenPriceProvider;

    private final BlockingBucket bucket;

    protected AbstractEthereumScanService() {
        Bandwidth limit = Bandwidth.simple(5, ofMillis(4500));
        bucket = Bucket.builder().addLimit(limit).build().asBlocking();
    }

    @Override
    public boolean accept(String address) {
        return address.startsWith("0x") && address.length() == 42;
    }

    @Override
    public List<TokenBalance> getAddressBalance(String address) {
        LOG.infof("Loading %s address balance %s", getNetwork(), address);
        try {
            Stream<TokenBalance> networkTokenBalance = Stream.of(getNetworkTokenBalanceAsTokenBalance(address));
            bucket.consume(1);
            Stream<TokenBalance> tokenBalances = getTokenEvents(address).stream()
                    .filter(new DistinctByKey<EthereumTokenEventResult>(x -> x.contractAddress)::filterByKey)
                    .map(x -> {
                        try {
                            bucket.consume(1);
                            return toAddressBalance(address, x, getTokenBalance(address, x.contractAddress));
                        } catch (InterruptedException e) {
                            LOG.error("BlockingBucket exception", e);
                            Thread.currentThread().interrupt();
                            return new TokenBalance(getNetwork(), LIQUID, ZERO, ZERO, "ERR", "Error");
                        }
                    });
            return Stream.concat(networkTokenBalance, tokenBalances)
                    .filter(x -> x.getUsdValue().compareTo(BigDecimal.valueOf(0.01)) > 0)
                    .toList();
        } catch (InterruptedException e) {
            LOG.error("BlockingBucket exception", e);
            Thread.currentThread().interrupt();
            return emptyList();
        }
    }

    private TokenBalance toAddressBalance(String address, EthereumTokenEventResult tokenEvent, EthereumTokenBalanceResult tokenBalance) {
        LOG.infof("Token balance for address %s on %s based on event %s: %s", address, getNetwork(), tokenEvent, tokenBalance.result);
        BigDecimal nativeValue = new BigDecimal(tokenBalance.result).divide((new BigDecimal(rightPad("1", parseInt(tokenEvent.tokenDecimal) + 1, '0'))), MathContext.DECIMAL64);
        BigDecimal usdValue = nativeValue.equals(ZERO) ? ZERO : nativeValue.multiply(BigDecimal.valueOf(tokenPriceProvider.getUsdValue(tokenEvent.tokenSymbol)));
        return new TokenBalance(getNetwork(), LIQUID, nativeValue, usdValue, tokenEvent.tokenSymbol, tokenEvent.tokenName);
    }

    protected abstract Network getNetwork();

    protected abstract EthereumTokenBalanceResult getTokenBalance(String address, String contractAddress);

    protected abstract List<EthereumTokenEventResult> getTokenEvents(String address);

    private TokenBalance getNetworkTokenBalanceAsTokenBalance(String address) throws InterruptedException {
        bucket.consume(1);
        NetworkTokenBalance balance = getNetworkTokenBalance(address);
        BigDecimal nativeValue = new BigDecimal(balance.nativeValue).divide((new BigDecimal(rightPad("1", balance.tokenDecimals + 1, '0'))), MathContext.DECIMAL64);
        BigDecimal usdValue = nativeValue.equals(ZERO) ? ZERO : nativeValue.multiply(BigDecimal.valueOf(tokenPriceProvider.getUsdValue(balance.tokenSymbol)));
        return new TokenBalance(getNetwork(), LIQUID, nativeValue, usdValue, balance.tokenSymbol, balance.tokenName);
    }

    protected abstract NetworkTokenBalance getNetworkTokenBalance(String address);

    protected static class NetworkTokenBalance {

        private final BigInteger nativeValue;
        private final String tokenSymbol;
        private final String tokenName;
        private final int tokenDecimals;

        public NetworkTokenBalance(BigInteger nativeValue, String tokenSymbol, String tokenName, int tokenDecimals) {
            this.nativeValue = nativeValue;
            this.tokenSymbol = tokenSymbol;
            this.tokenName = tokenName;
            this.tokenDecimals = tokenDecimals;
        }
    }
}
