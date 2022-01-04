package ch.grignola.service.scanner.cro;

import ch.grignola.model.Network;
import ch.grignola.service.quote.TokenPriceProvider;
import ch.grignola.service.scanner.TokenBalance;
import ch.grignola.service.scanner.cro.model.CroBalanceResult;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import static ch.grignola.model.Allocation.LIQUID;
import static java.math.BigDecimal.ZERO;
import static java.util.Collections.singletonList;

@ApplicationScoped
public class CroScanServiceImpl implements CroScanService {

    private static final Logger LOG = Logger.getLogger(CroScanServiceImpl.class);

    @Inject
    protected TokenPriceProvider tokenPriceProvider;

    @Inject
    @RestClient
    CroRestClient croRestClient;

    @Override
    public boolean accept(String address) {
        return address.startsWith("cro") && address.length() == 42;
    }

    @Override
    public List<TokenBalance> getAddressBalance(String address) {
        CroBalanceResult result = croRestClient.getBalance(address);
        BigDecimal nativeValue = result.result.totalBalance.stream().findFirst().map(x -> new BigDecimal(x.amount).divide(new BigDecimal("100000000"), MathContext.DECIMAL64)).orElse(ZERO);
        BigDecimal usdValue = nativeValue.equals(ZERO) ? ZERO : nativeValue.multiply(BigDecimal.valueOf(tokenPriceProvider.getUsdValue("CRO")));
        LOG.infof("Token balance for address %s on Terra: %s (%s USD)", address, nativeValue, usdValue);
        return singletonList(new TokenBalance(Network.CRO, LIQUID, nativeValue, usdValue, "CRO", "Crypto.com Coin"));
    }
}
