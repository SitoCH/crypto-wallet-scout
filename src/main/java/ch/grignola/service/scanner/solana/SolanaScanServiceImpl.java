package ch.grignola.service.scanner.solana;

import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.solana.model.SolanaNativeBalance;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import static ch.grignola.model.Allocation.LIQUID;
import static ch.grignola.model.Network.SOLANA;
import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang3.StringUtils.rightPad;

@ApplicationScoped
public class SolanaScanServiceImpl implements SolanaScanService {

    private static final Logger LOG = Logger.getLogger(SolanaScanServiceImpl.class);
    private static final String SYMBOL = "SOL";

    @Inject
    @RestClient
    SolanaRestClient solanaRestClient;

    @Override
    public boolean accept(String address) {
        return !address.startsWith("terra") && address.length() == 44;
    }

    @Override
    public List<ScannerTokenBalance> getAddressBalance(String address) {

        SolanaNativeBalance nativeBalance = solanaRestClient.getNativeBalance(address);

        List<ScannerTokenBalance> balances = new ArrayList<>();

        BigDecimal nativeValue = BigDecimal.valueOf(nativeBalance.lamports);
        if (nativeValue.compareTo(ZERO) != 0) {
            balances.add(toTokenBalance(address, SYMBOL, nativeValue, 9));
        }

        balances.addAll(solanaRestClient.getAccountTokens(address).stream()
                .map(x -> toTokenBalance(address, x.tokenSymbol, new BigDecimal(x.tokenAmount.amount), x.tokenAmount.decimals))
                .toList());

        return balances;
    }

    private ScannerTokenBalance toTokenBalance(String address, String symbol, BigDecimal value, int tokenDecimals) {
        BigDecimal nativeValue = value.divide(new BigDecimal(rightPad("1", tokenDecimals + 1, '0')), MathContext.DECIMAL64);
        LOG.infof("Token balance for address %s on Solana: %s %s", address, nativeValue, symbol);
        return new ScannerTokenBalance(SOLANA, LIQUID, nativeValue, symbol);
    }
}
