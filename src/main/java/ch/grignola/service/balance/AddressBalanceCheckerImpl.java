package ch.grignola.service.balance;

import ch.grignola.model.Allocation;
import ch.grignola.service.scanner.avalanche.AvalancheEtherscanService;
import ch.grignola.service.scanner.bitcoin.BitcoinScanService;
import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.cosmos.CosmosScanService;
import ch.grignola.service.scanner.cronos.CronosScanService;
import ch.grignola.service.scanner.optimism.OptimismScanService;
import ch.grignola.service.scanner.polkadot.PolkadotScanService;
import ch.grignola.service.scanner.polygon.PolygonBitqueryService;
import ch.grignola.service.scanner.solana.SolanaScanService;
import ch.grignola.service.scanner.terra.TerraClassicScanService;
import ch.grignola.service.scanner.terra.TerraScanService;
import ch.grignola.service.token.TokenProvider;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.ZERO;
import static java.util.Comparator.comparing;


@ApplicationScoped
public class AddressBalanceCheckerImpl implements AddressBalanceChecker {

    private static final Logger LOG = Logger.getLogger(AddressBalanceCheckerImpl.class);

    @Inject
    TokenProvider tokenProvider;
    @Inject
    PolygonBitqueryService polygonService;
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
    PolkadotScanService polkadotScanService;
    @Inject
    OptimismScanService optimismScanService;

    private List<ScanService> getScanServices() {
        return List.of(polygonService, avalancheService, terraScanService, terraClassicScanService,
                cronosScanService, solanaScanService, cosmosScanService, bitcoinScanService, polkadotScanService,
                optimismScanService);
    }

    private List<ScannerTokenBalance> getBalancesFromScanServices(String address) {
        return getScanServices().stream()
                .filter(x -> x.accept(address))
                .flatMap(x -> x.getAddressBalance(address).stream())
                .toList();
    }

    @Override
    @Transactional
    public List<TokenBalance> getBalances(List<String> addresses) {
        List<ScannerTokenBalance> rawBalances = addresses.stream()
                .flatMap(x -> getBalancesFromScanServices(x).stream())
                .toList();
        return getTokenBalances(rawBalances);
    }

    @Override
    @Transactional
    public List<TokenBalance> getBalance(String address) {
        return getTokenBalances(getBalancesFromScanServices(address));
    }

    private List<TokenBalance> getTokenBalances(List<ScannerTokenBalance> rawBalances) {
        return rawBalances.stream()
                .filter(x -> x.nativeValue().compareTo(ZERO) != 0)
                .map(this::toTokenBalance)
                .filter(x -> x != null && x.getUsdValue().compareTo(BigDecimal.valueOf(0.01)) > 0)
                .sorted(comparing(TokenBalance::getTokenId))
                .toList();
    }

    private TokenBalance toTokenBalance(ScannerTokenBalance balance) {
        return tokenProvider.getBySymbol(balance.tokenSymbol())
                .map(tokenDetail -> {
                    LOG.infof("Token %s: 1 USD - %f %s", tokenDetail.name(), tokenDetail.usdValue(), tokenDetail.symbol());
                    Allocation allocation = tokenDetail.allocation() != null ? tokenDetail.allocation() : balance.allocation();
                    BigDecimal usdValue = balance.nativeValue().multiply(BigDecimal.valueOf(tokenDetail.usdValue()));
                    return new TokenBalance(balance.network(), allocation, balance.nativeValue(), usdValue, tokenDetail.id(), tokenDetail.parentId());
                })
                .orElse(null);
    }
}
