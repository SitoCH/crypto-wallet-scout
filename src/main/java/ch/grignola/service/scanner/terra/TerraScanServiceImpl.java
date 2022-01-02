package ch.grignola.service.scanner.terra;

import ch.grignola.model.Network;
import ch.grignola.service.quote.TokenPriceProvider;
import ch.grignola.service.scanner.model.TokenBalance;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import static java.math.BigDecimal.ZERO;
import static java.util.Collections.singletonList;

@ApplicationScoped
public class TerraScanServiceImpl implements TerraScanService {

    private static final Logger LOG = Logger.getLogger(TerraScanServiceImpl.class);

    @Inject
    protected TokenPriceProvider tokenPriceProvider;

    @Inject
    @RestClient
    TerraRestClient terraRestClient;

    @Override
    public boolean accept(String address) {
        return address.startsWith("terra") && address.length() == 44;
    }

    @Override
    public List<TokenBalance> getAddressBalance(String address) {
        TerraBalanceResult result = terraRestClient.getBalance(address);
        BigDecimal nativeValue = result.balance.stream().findFirst().map(x -> new BigDecimal(x.available).divide(new BigDecimal("1000000"), MathContext.DECIMAL64)).orElse(ZERO);
        BigDecimal usdValue = nativeValue.equals(ZERO) ? ZERO : nativeValue.multiply(BigDecimal.valueOf(tokenPriceProvider.getUsdValue("LUNA")));
        LOG.infof("Token balance for address %s on Terra: %s (%s USD)", address, nativeValue, usdValue);
        return singletonList(new TokenBalance(Network.TERRA, nativeValue, usdValue, "LUNA", "Terra"));
    }
}
