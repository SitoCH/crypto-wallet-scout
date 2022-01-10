package ch.grignola.service.token;

import ch.grignola.model.Token;
import ch.grignola.repository.TokenRepository;
import ch.grignola.service.token.model.CoingeckoCoinDetail;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

@ApplicationScoped
public class TokenPriceProviderImpl implements TokenPriceProvider {

    private static final Logger LOG = Logger.getLogger(TokenPriceProviderImpl.class);

    @Inject
    TokenRepository tokenRepository;

    @Inject
    @RestClient
    CoingeckoRestClient coingeckoRestClient;

    private CoingeckoCoinDetail refreshTokenInfo(CoingeckoCoinDetail coin, Token token) {

        if (coin.name.equals(token.getName())) {
            token.setName(coin.name);
            tokenRepository.persist(token);
        }

        return coin;
    }

    public double getUsdValue(String symbol) {

        Token token = tokenRepository.findBySymbol(symbol).orElseGet(() -> {
            Token newToken = new Token();
            newToken.setSymbol(symbol);
            tokenRepository.persist(newToken);
            return newToken;
        });

        String symbolToUse = firstNonNull(token.getCoinGeckoSymbol(), token.getSymbol());

        return coingeckoRestClient.getCoins().stream()
                .filter(x -> x.symbol.equalsIgnoreCase(symbolToUse))
                .findFirst()
                .map(x -> coingeckoRestClient.get(x.id))
                .map(x -> refreshTokenInfo(x, token))
                .map(x -> x.marketData.currentPrice.usd)
                .orElseGet(() -> {
                    LOG.warnf("Missing coin quote for %s", symbol);
                    return 0f;
                });
    }
}
