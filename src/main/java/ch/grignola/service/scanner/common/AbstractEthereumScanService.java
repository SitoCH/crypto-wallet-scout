package ch.grignola.service.scanner.common;

import ch.grignola.model.Network;
import ch.grignola.service.quote.TokenPriceProvider;
import ch.grignola.service.scanner.model.TokenBalance;
import ch.grignola.utils.DistinctByKey;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BlockingBucket;
import io.github.bucket4j.Bucket;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.math.BigDecimal.ZERO;
import static java.time.Duration.ofMillis;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.rightPad;

public abstract class AbstractEthereumScanService implements ScanService {

    private static final Logger LOG = Logger.getLogger(AbstractEthereumScanService.class);

    @Inject
    TokenPriceProvider tokenPriceProvider;

    @Override
    public boolean accept(String address) {
        return address.startsWith("0x") && address.length() == 42;
    }

    @Override
    public List<TokenBalance> getAddressBalance(String address) {
        Bandwidth limit = Bandwidth.simple(4, ofMillis(2500));
        BlockingBucket bucket = Bucket.builder().addLimit(limit).build().asBlocking();

        LOG.infof("Loading %s address balance %s", getNetwork(), address);
        return getTokenEvents(address)
                .stream()
                .filter(new DistinctByKey<EthereumTokenEventResult>(x -> x.contractAddress)::filterByKey)
                .map(x -> {
                    try {
                        bucket.consume(1);
                        return toAddressBalance(address, x, getTokenBalance(address, x.contractAddress));
                    } catch (InterruptedException e) {
                        LOG.error("BlockingBucket exception", e);
                        Thread.currentThread().interrupt();
                        return new TokenBalance(getNetwork(), ZERO, ZERO, "ERR", "Error");
                    }
                })
                .filter(x -> x.getNativeValue().compareTo(ZERO) != 0)
                .collect(toList());
    }

    private TokenBalance toAddressBalance(String address, EthereumTokenEventResult tokenEvent, EthereumTokenBalanceResult tokenBalance) {
        LOG.infof("Token balance for address %s on %s based on event %s: %s", address, getNetwork(), tokenEvent, tokenBalance.result);
        BigDecimal nativeValue = new BigDecimal(tokenBalance.result).divide((new BigDecimal(rightPad("1", parseInt(tokenEvent.tokenDecimal) + 1, '0'))), MathContext.DECIMAL64);
        BigDecimal usdValue = nativeValue.multiply(BigDecimal.valueOf(tokenPriceProvider.getUsdValue(tokenEvent.tokenSymbol)));
        return new TokenBalance(getNetwork(), nativeValue, usdValue, tokenEvent.tokenSymbol, tokenEvent.tokenName);
    }

    protected abstract Network getNetwork();

    protected abstract EthereumTokenBalanceResult getTokenBalance(String address, String contractAddress);

    protected abstract List<EthereumTokenEventResult> getTokenEvents(String address);
}
