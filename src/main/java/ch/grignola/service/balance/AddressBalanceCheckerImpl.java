package ch.grignola.service.balance;

import ch.grignola.model.Allocation;
import ch.grignola.model.BannedContract;
import ch.grignola.model.Network;
import ch.grignola.repository.BannedContractRepository;
import ch.grignola.service.scanner.avalanche.AvalancheScanService;
import ch.grignola.service.scanner.bitcoin.BitcoinScanService;
import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.cosmos.CosmosScanService;
import ch.grignola.service.scanner.cronos.CronosScanService;
import ch.grignola.service.scanner.polkadot.PolkadotScanService;
import ch.grignola.service.scanner.polygon.PolygonScanService;
import ch.grignola.service.scanner.solana.SolanaScanService;
import ch.grignola.service.scanner.terra.TerraScanService;
import ch.grignola.service.token.TokenProvider;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.math.BigDecimal.ZERO;
import static java.util.Comparator.comparing;


@ApplicationScoped
public class AddressBalanceCheckerImpl implements AddressBalanceChecker {

    private static final Logger LOG = Logger.getLogger(AddressBalanceCheckerImpl.class);

    @Inject
    ManagedExecutor executor;
    @Inject
    BannedContractRepository bannedContractRepository;
    @Inject
    TokenProvider tokenProvider;
    @Inject
    PolygonScanService polygonScanService;
    @Inject
    AvalancheScanService avalancheScanService;
    @Inject
    TerraScanService terraScanService;
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

    private List<ScanService> getScanServices() {
        return List.of(polygonScanService, avalancheScanService, terraScanService, cronosScanService, solanaScanService,
                cosmosScanService, bitcoinScanService, polkadotScanService);
    }

    private List<ScannerTokenBalance> getBalancesFromScanServices(String address, Map<Network, List<BannedContract>> bannedContracts) {
        return getScanServices().stream()
                .filter(x -> x.accept(address))
                .map(x -> executor.supplyAsync(() -> x.getAddressBalance(address, bannedContracts).stream()))
                .toList().stream()
                .flatMap(CompletableFuture::join)
                .toList();
    }

    @Override
    public List<TokenBalance> getBalances(List<String> addresses) {
        Map<Network, List<BannedContract>> bannedContracts = bannedContractRepository.findAllByNetwork();
        List<ScannerTokenBalance> rawBalances = addresses.stream()
                .map(x -> executor.supplyAsync(() -> getBalancesFromScanServices(x, bannedContracts).stream()))
                .toList().stream()
                .flatMap(CompletableFuture::join)
                .toList();
        return getTokenBalances(rawBalances);
    }

    @Override
    public List<TokenBalance> getBalance(String address) {
        Map<Network, List<BannedContract>> bannedContracts = bannedContractRepository.findAllByNetwork();
        return getTokenBalances(getBalancesFromScanServices(address, bannedContracts));
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
                    LOG.infof("Token %s: 1 USD - %f %s", tokenDetail.getName(), tokenDetail.getUsdValue(), tokenDetail.getSymbol());
                    Allocation allocation = tokenDetail.getAllocation() != null ? tokenDetail.getAllocation() : balance.allocation();
                    BigDecimal usdValue = balance.nativeValue().multiply(BigDecimal.valueOf(tokenDetail.getUsdValue()));
                    return new TokenBalance(balance.network(), allocation, balance.nativeValue(), usdValue, tokenDetail.getId(), tokenDetail.getParentId());
                })
                .orElse(null);
    }
}
