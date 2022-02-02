package ch.grignola.service.scanner.ethereum;

import ch.grignola.model.Network;
import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.utils.DistinctByKey;
import io.github.bucket4j.BlockingBucket;
import io.github.bucket4j.Bucket;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.List;
import java.util.stream.Stream;

import static ch.grignola.model.Allocation.LIQUID;
import static io.github.bucket4j.Bandwidth.classic;
import static io.github.bucket4j.Refill.intervally;
import static java.lang.Integer.parseInt;
import static java.math.BigDecimal.ZERO;
import static java.time.Duration.ofMillis;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.rightPad;

public abstract class AbstractEthereumScanService implements ScanService {

    private static final Logger LOG = Logger.getLogger(AbstractEthereumScanService.class);

    protected final BlockingBucket bucket;

    protected AbstractEthereumScanService() {
        bucket = Bucket.builder()
                .addLimit(classic(4, intervally(4, ofMillis(900))))
                .build().asBlocking();
    }

    @Override
    public boolean accept(String address) {
        return address.startsWith("0x") && address.length() == 42;
    }

    @Override
    public List<ScannerTokenBalance> getAddressBalance(String address) {
        try {
            Stream<ScannerTokenBalance> networkTokenBalance = Stream.of(getNetworkTokenBalanceAsTokenBalance(address));
            Stream<ScannerTokenBalance> tokenBalances = getTokenEvents(address).stream()
                    .filter(new DistinctByKey<EthereumTokenEventResult>(x -> x.contractAddress)::filterByKey)
                    .map(x -> {
                        try {
                            return toAddressBalance(address, x, getTokenBalance(address, x.contractAddress));
                        } catch (InterruptedException e) {
                            LOG.error("BlockingBucket exception", e);
                            Thread.currentThread().interrupt();
                            return new ScannerTokenBalance(getNetwork(), LIQUID, ZERO, "ERR");
                        }
                    });
            return Stream.concat(networkTokenBalance, tokenBalances)
                    .filter(x -> x.getNativeValue().compareTo(BigDecimal.valueOf(0.01)) > 0)
                    .toList();
        } catch (InterruptedException e) {
            LOG.error("BlockingBucket exception", e);
            Thread.currentThread().interrupt();
            return emptyList();
        }
    }

    private ScannerTokenBalance toAddressBalance(String address, EthereumTokenEventResult tokenEvent, EthereumTokenBalanceResult tokenBalance) {
        LOG.infof("Token balance for address %s on %s based on event for symbol %s (%s): %s", address, getNetwork(), tokenEvent.tokenSymbol, tokenEvent.contractAddress, tokenBalance.result);
        BigDecimal nativeValue = new BigDecimal(tokenBalance.result).divide((new BigDecimal(rightPad("1", parseInt(tokenEvent.tokenDecimal) + 1, '0'))), MathContext.DECIMAL64);
        return new ScannerTokenBalance(getNetwork(), LIQUID, nativeValue, tokenEvent.tokenSymbol);
    }

    protected abstract Network getNetwork();

    protected abstract EthereumTokenBalanceResult getTokenBalance(String address, String contractAddress) throws InterruptedException;

    protected abstract List<EthereumTokenEventResult> getTokenEvents(String address) throws InterruptedException;

    private ScannerTokenBalance getNetworkTokenBalanceAsTokenBalance(String address) throws InterruptedException {
        NetworkTokenBalance balance = getNetworkTokenBalance(address);
        BigDecimal nativeValue = new BigDecimal(balance.nativeValue).divide(new BigDecimal(rightPad("1", balance.tokenDecimals + 1, '0')), MathContext.DECIMAL64);
        return new ScannerTokenBalance(getNetwork(), LIQUID, nativeValue, balance.tokenSymbol);
    }

    protected abstract NetworkTokenBalance getNetworkTokenBalance(String address) throws InterruptedException;

    protected static class NetworkTokenBalance {

        private final BigInteger nativeValue;
        private final String tokenSymbol;
        private final int tokenDecimals;

        public NetworkTokenBalance(BigInteger nativeValue, String tokenSymbol, int tokenDecimals) {
            this.nativeValue = nativeValue;
            this.tokenSymbol = tokenSymbol;
            this.tokenDecimals = tokenDecimals;
        }
    }
}
