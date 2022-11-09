package ch.grignola.service.balance;

import ch.grignola.service.scanner.aave.AaveScanService;
import ch.grignola.service.scanner.avalanche.AvalancheEtherscanService;
import ch.grignola.service.scanner.bitcoin.BitcoinScanService;
import ch.grignola.service.scanner.bnb.BnbEtherscanService;
import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScanServiceException;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.cosmos.CosmosScanService;
import ch.grignola.service.scanner.cronos.CronosScanService;
import ch.grignola.service.scanner.dogecoin.DogecoinScanService;
import ch.grignola.service.scanner.ethereum.EthereumEtherscanService;
import ch.grignola.service.scanner.optimism.OptimismScanService;
import ch.grignola.service.scanner.polygon.PolygonEtherscanService;
import ch.grignola.service.scanner.solana.SolanaScanService;
import ch.grignola.service.scanner.terra.TerraClassicScanService;
import ch.grignola.service.scanner.terra.TerraScanService;
import ch.grignola.service.token.TokenProvider;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.util.Comparator.comparing;


@ApplicationScoped
public class AddressBalanceCheckerImpl implements AddressBalanceChecker {

    private static final Logger LOG = Logger.getLogger(AddressBalanceCheckerImpl.class);

    @Inject
    TokenProvider tokenProvider;
    @Inject
    PolygonEtherscanService polygonService;
    @Inject
    AvalancheEtherscanService avalancheService;
    @Inject
    TerraScanService terraScanService;
    @Inject
    TerraClassicScanService terraClassicScanService;
    @Inject
    SolanaScanService solanaScanService;
    @Inject
    CronosScanService cronosScanService;
    @Inject
    CosmosScanService cosmosScanService;
    @Inject
    BitcoinScanService bitcoinScanService;
    @Inject
    OptimismScanService optimismScanService;
    @Inject
    EthereumEtherscanService ethereumEtherscanService;
    @Inject
    AaveScanService aaveScanService;
    @Inject
    DogecoinScanService dogecoinScanService;
    @Inject
    BnbEtherscanService bnbEtherscanService;

    private List<ScanService> getScanServices() {
        return List.of(polygonService, avalancheService, terraScanService, terraClassicScanService,
                cronosScanService, solanaScanService, cosmosScanService, bitcoinScanService, bnbEtherscanService,
                optimismScanService, aaveScanService, ethereumEtherscanService, dogecoinScanService);
    }

    private List<ScannerTokenBalance> getBalancesFromScanServices(String address) {
        return getScanServices().stream()
                .filter(x -> x.accept(address))
                .flatMap(x -> {
                    try {
                        return x.getAddressBalance(address).stream();
                    } catch (Exception ex) {
                        throw new ScanServiceException(format("%s: %s", x.getClass().getTypeName(), ex.getMessage()));
                    }
                })
                .toList();
    }

    @Override
    @Transactional
    public TokenBalances getBalances(List<String> addresses) {
        List<TokenBalances.TokenBalanceError> errors = new ArrayList<>();
        List<ScannerTokenBalance> rawBalances = addresses.stream()
                .flatMap(address -> {
                    try {
                        return getBalancesFromScanServices(address).stream();
                    } catch (Exception ex) {
                        LOG.errorf(ex, "Error on balance check for address %s", address);
                        errors.add(new TokenBalances.TokenBalanceError(address, ex.getMessage()));
                        return Stream.empty();
                    }
                })
                .toList();
        return new TokenBalances(getTokenBalances(rawBalances), errors);
    }

    @Override
    public List<TokenBalance> getBalance(String address) {
        return getTokenBalances(getBalancesFromScanServices(address));
    }

    private List<TokenBalance> getTokenBalances(List<ScannerTokenBalance> rawBalances) {
        return rawBalances.stream()
                .filter(x -> x.nativeValue().compareTo(ZERO) != 0)
                .map(this::toTokenBalance)
                .filter(x -> x != null && (x.getUsdValue().compareTo(valueOf(0.01)) > 0 || x.getUsdValue().compareTo(valueOf(-0.01)) < 0))
                .sorted(comparing(TokenBalance::getTokenId))
                .toList();
    }

    private TokenBalance toTokenBalance(ScannerTokenBalance balance) {
        return tokenProvider.getBySymbol(balance.tokenSymbol())
                .map(tokenDetail -> {
                    LOG.infof("Token %s: 1 USD - %f %s", tokenDetail.name(), tokenDetail.usdValue(), tokenDetail.symbol());
                    BigDecimal usdValue = balance.nativeValue().multiply(valueOf(tokenDetail.usdValue()));
                    return new TokenBalance(balance.network(), balance.allocation(), balance.nativeValue(), usdValue, tokenDetail.id(), tokenDetail.parentId());
                })
                .orElse(null);
    }
}
