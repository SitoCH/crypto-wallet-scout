package ch.grignola.service.scanner.etherscan;

import ch.grignola.model.AddressTokenValue;
import ch.grignola.model.Network;
import ch.grignola.repository.AddressTokenValueRepository;
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
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static ch.grignola.model.Allocation.LIQUID;
import static java.math.BigDecimal.ZERO;
import static java.time.OffsetDateTime.ofInstant;
import static java.time.ZoneOffset.UTC;
import static java.util.Comparator.comparing;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.rightPad;


public abstract class AbstractEtherscanScanService extends AbstractScanService implements ScanService {
    private static final Logger LOG = Logger.getLogger(AbstractEtherscanScanService.class);

    protected final RateLimiter rateLimiter;

    private final Network network;

    @Inject
    AddressTokenValueRepository addressTokenValueRepository;

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

    private boolean filterContracts(Set<String> bannedContracts, String address, EthereumTokenEventResult balance) {
        if (bannedContracts.contains(balance.contractAddress)) {
            LOG.infof("Found banned contract for address %s on %s: %s (%s)", address, network, balance.tokenSymbol, balance.contractAddress);
            return false;
        }

        if (containsIgnoreCase(balance.tokenName, "AAVE")) {
            LOG.infof("Found AAVE token for address %s on %s: %s (%s)", address, network, balance.tokenSymbol, balance.contractAddress);
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
        Stream<ScannerTokenBalance> tokenBalances = getTokenBalances(contractStatus, address);
        return Stream.concat(networkTokenBalance, tokenBalances)
                .filter(x -> !x.nativeValue().equals(ZERO))
                .toList();
    }

    private Stream<ScannerTokenBalance> getTokenBalances(ContractStatus contractStatus, String address) {
        List<EthereumTokenEventResult> tokenEvents = getTokenEvents(address);

        OffsetDateTime lastDatabaseDateTime = addressTokenValueRepository.getLastUpdated(network, address)
                .orElse(OffsetDateTime.MIN);

        OffsetDateTime lastEtherscanDateTime = tokenEvents.stream()
                .max(comparing(x -> x.timeStamp))
                .map(x -> ofInstant(Instant.ofEpochSecond(x.timeStamp), UTC))
                .orElse(OffsetDateTime.MIN);

        boolean fetchFromEtherscan = lastEtherscanDateTime.isAfter(lastDatabaseDateTime);

        if (fetchFromEtherscan) {
            addressTokenValueRepository.delete(network, address);
            return tokenEvents.stream()
                    .filter(new DistinctByKey<EthereumTokenEventResult>(x -> x.contractAddress)::filterByKey)
                    .filter(x -> filterContracts(contractStatus.bannedContracts(), address, x))
                    .map(x -> {
                        checkContractVerificationStatus(contractStatus.allVerifiedContracts(), network, x.contractAddress, x.tokenSymbol);
                        EthereumTokenBalanceResult tokenBalance = getTokenBalance(address, x.contractAddress);
                        LOG.infof("Token balance for address %s on %s based on event for symbol %s (%s): %s", address, network, x.tokenSymbol, x.contractAddress, tokenBalance.result);
                        BigDecimal nativeValue = new BigDecimal(tokenBalance.result).divide((new BigDecimal(rightPad("1", x.tokenDecimal + 1, '0'))), MathContext.DECIMAL64);
                        saveCurrentNativeValue(address, lastEtherscanDateTime, x.tokenSymbol, nativeValue);
                        return new ScannerTokenBalance(network, LIQUID, nativeValue, x.tokenSymbol);
                    });
        }

        return addressTokenValueRepository.find(network, address)
                .stream().map(x -> {
                    LOG.infof("Token balance for address %s on %s based on event for symbol %s: %s", address, network, x.getTokenSymbol(), x.getNativeValue());
                    return new ScannerTokenBalance(network, LIQUID, x.getNativeValue(), x.getTokenSymbol());
                });
    }

    private void saveCurrentNativeValue(String address, OffsetDateTime dateTime, String tokenSymbol, BigDecimal nativeValue) {
        AddressTokenValue addressTokenValue = new AddressTokenValue();
        addressTokenValue.setNetwork(network);
        addressTokenValue.setAddress(address);
        addressTokenValue.setDateTime(dateTime);
        addressTokenValue.setTokenSymbol(tokenSymbol);
        addressTokenValue.setNativeValue(nativeValue);
        addressTokenValueRepository.persist(addressTokenValue);
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
