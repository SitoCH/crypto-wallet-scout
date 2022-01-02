package ch.grignola.service.quote;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TokenPriceProviderImpl implements TokenPriceProvider {

    private static final Logger LOG = Logger.getLogger(TokenPriceProviderImpl.class);

    @Inject
    @RestClient
    CoingeckoRestClient coingeckoRestClient;

    public double getUsdValue(String symbol) {
        return coingeckoRestClient.getCoins().stream()
                .filter(x -> x.symbol.equalsIgnoreCase(symbol))
                .findFirst()
                .map(x -> coingeckoRestClient.getTicker(x.id))
                .flatMap(x -> x.tickers.stream().findFirst())
                .map(x -> x.convertedLast.usd)
                .orElseGet(() -> {
                    LOG.warnf("Missing coin quote for %s", symbol);
                    return 0f;
                });
    }
}
