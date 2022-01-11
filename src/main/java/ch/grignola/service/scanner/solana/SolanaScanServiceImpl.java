package ch.grignola.service.scanner.solana;

import ch.grignola.service.scanner.TokenBalance;
import ch.grignola.service.scanner.solana.model.SolanaNativeBalance;
import ch.grignola.service.token.TokenProvider;
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
    protected TokenProvider tokenProvider;

    @Inject
    @RestClient
    SolanaRestClient solanaRestClient;

    @Override
    public boolean accept(String address) {
        return !address.startsWith("terra") && address.length() == 44;
    }

    @Override
    public List<TokenBalance> getAddressBalance(String address) {
        String image = tokenProvider.getImageSmall(SYMBOL);

        SolanaNativeBalance nativeBalance = solanaRestClient.getNativeBalance(address);

        List<TokenBalance> balances = new ArrayList<>();

        BigDecimal nativeValue = BigDecimal.valueOf(nativeBalance.lamports);
        if (nativeValue.compareTo(ZERO) != 0) {
            balances.add(toTokenBalance(address, image, SYMBOL, "Solana", nativeValue, 9));
        }

        balances.addAll(solanaRestClient.getAccountTokens(address).stream()
                .map(x -> toTokenBalance(address, tokenProvider.getImageSmall(x.tokenSymbol), x.tokenSymbol, x.tokenName, new BigDecimal(x.tokenAmount.amount), x.tokenAmount.decimals))
                .toList());

        return balances;
    }

    private TokenBalance toTokenBalance(String address, String image, String symbol, String name, BigDecimal value, int tokenDecimals) {
        BigDecimal nativeValue = value.divide(new BigDecimal(rightPad("1", tokenDecimals + 1, '0')), MathContext.DECIMAL64);
        BigDecimal usdValue = nativeValue.equals(ZERO) ? ZERO : nativeValue.multiply(BigDecimal.valueOf(tokenProvider.getUsdValue(symbol)));
        LOG.infof("Token balance for address %s on Solana: %s (%s USD)", address, nativeValue, usdValue);
        return new TokenBalance(SOLANA, LIQUID, nativeValue, usdValue, symbol, name, image);
    }
}
