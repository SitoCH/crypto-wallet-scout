package ch.grignola.service.balance;

import ch.grignola.model.Allocation;
import ch.grignola.model.BannedContract;
import ch.grignola.model.Network;
import ch.grignola.repository.BannedContractRepository;
import ch.grignola.service.scanner.avalanche.AvalancheScanService;
import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.cosmos.CosmosScanService;
import ch.grignola.service.scanner.cronos.CronosScanService;
import ch.grignola.service.scanner.polygon.PolygonScanService;
import ch.grignola.service.scanner.solana.SolanaScanService;
import ch.grignola.service.scanner.terra.TerraScanService;
import ch.grignola.service.token.TokenProvider;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ZERO;
import static java.util.Comparator.comparing;


@ApplicationScoped
public class AddressBalanceCheckerImpl implements AddressBalanceChecker {

    private static final Logger LOG = Logger.getLogger(AddressBalanceCheckerImpl.class);

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
    BannedContractRepository bannedContractRepository;

    private List<ScanService> getScanServices() {
        return List.of(polygonScanService, avalancheScanService, terraScanService, cronosScanService, solanaScanService, cosmosScanService);
    }

    private List<ScannerTokenBalance> getBalancesFromScanServices(String address, Map<Network, List<BannedContract>> bannedContracts) {
        return getScanServices().parallelStream()
                .filter(x -> x.accept(address))
                .flatMap(x -> x.getAddressBalance(address, bannedContracts).stream())
                .toList();
    }

    @Override
    public List<TokenBalance> getBalances(List<String> addresses) {
        Map<Network, List<BannedContract>> bannedContracts = bannedContractRepository.findAllByNetwork();
        List<ScannerTokenBalance> rawBalances = addresses.parallelStream()
                .flatMap(x -> getBalancesFromScanServices(x, bannedContracts).stream())
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
                .map(this::toTokenBalance)
                .filter(x -> x != null && x.getUsdValue().compareTo(BigDecimal.valueOf(0.01)) > 0)
                .sorted(comparing(TokenBalance::getTokenId))
                .toList();
    }

    private TokenBalance toTokenBalance(ScannerTokenBalance balance) {
        return tokenProvider.getBySymbol(balance.getTokenSymbol())
                .map(tokenDetail -> {
                    LOG.infof("Token %s: 1 USD - %f %s", tokenDetail.getName(), tokenDetail.getUsdValue(), tokenDetail.getSymbol());
                    Allocation allocation = tokenDetail.getAllocation() != null ? tokenDetail.getAllocation() : balance.getAllocation();
                    BigDecimal usdValue = balance.getNativeValue().equals(ZERO) ? ZERO : balance.getNativeValue().multiply(BigDecimal.valueOf(tokenDetail.getUsdValue()));
                    return new TokenBalance(balance.getNetwork(), allocation, balance.getNativeValue(), usdValue, tokenDetail.getId(), tokenDetail.getParentId());
                })
                .orElse(null);
    }
}
