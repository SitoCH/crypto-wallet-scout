package ch.grignola.service.balance;

import ch.grignola.model.Allocation;
import ch.grignola.service.scanner.avalanche.AvalancheScanService;
import ch.grignola.service.scanner.common.ScanService;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.cro.CroScanService;
import ch.grignola.service.scanner.polygon.PolygonScanService;
import ch.grignola.service.scanner.solana.SolanaScanService;
import ch.grignola.service.scanner.terra.TerraScanService;
import ch.grignola.service.token.TokenProvider;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.ZERO;


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
    CroScanService croScanService;

    private List<ScanService> getScanServices() {
        return List.of(polygonScanService, avalancheScanService, terraScanService, croScanService, solanaScanService);
    }

    private List<ScannerTokenBalance> getBalancesFromScanServices(String address) {
        return getScanServices().stream()
                .filter(x -> x.accept(address))
                .flatMap(x -> x.getAddressBalance(address).stream())
                .toList();
    }

    @Override
    public AddressBalance getAddressBalance(String address) {
        return new AddressBalance(getBalancesFromScanServices(address).stream()
                .sorted((a, b) -> a.getTokenSymbol().compareToIgnoreCase(b.getTokenSymbol()))
                .map(this::toAddressBalance)
                .filter(x -> x != null && x.getUsdValue().compareTo(BigDecimal.valueOf(0.01)) > 0)
                .toList());
    }

    private TokenBalance toAddressBalance(ScannerTokenBalance balance) {
        return tokenProvider.getBySymbol(balance.getTokenSymbol())
                .map(tokenDetail -> {
                    LOG.infof("Token %s: 1 USD - %f %s", tokenDetail.getName(), tokenDetail.getUsdValue(), tokenDetail.getSymbol());
                    Allocation allocation = tokenDetail.getAllocation() != null ? tokenDetail.getAllocation() : balance.getAllocation();
                    BigDecimal usdValue = balance.getNativeValue().equals(ZERO) ? ZERO : balance.getNativeValue().multiply(BigDecimal.valueOf(tokenDetail.getUsdValue()));
                    return new TokenBalance(balance.getNetwork(), allocation, balance.getNativeValue(), usdValue, tokenDetail.getId());
                })
                .orElse(null);
    }
}
