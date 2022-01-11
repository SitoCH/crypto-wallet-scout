package ch.grignola.service.token;

import ch.grignola.model.Token;
import ch.grignola.repository.TokenRepository;
import ch.grignola.service.token.model.CoingeckoCoinDetail;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

@ApplicationScoped
public class TokenProviderImpl implements TokenProvider {

    private static final Logger LOG = Logger.getLogger(TokenProviderImpl.class);

    @Inject
    TokenRepository tokenRepository;

    @Inject
    @RestClient
    CoingeckoRestClient coingeckoRestClient;

    public String getImageSmall(String symbol) {
        return getTokenWithUsdValue(symbol)
                .map(x -> x.token.getImageSmall())
                .orElseGet(() -> {
                    LOG.warnf("Missing coin detail for %s", symbol);
                    return null;
                });
    }

    public double getUsdValue(String symbol) {
        return getTokenWithUsdValue(symbol)
                .map(x -> x.usdValue)
                .orElseGet(() -> {
                    LOG.warnf("Missing coin detail for %s", symbol);
                    return 0f;
                });
    }

    private Optional<TokenWithUsdValue> getTokenWithUsdValue(String symbol) {
        return tokenRepository.findBySymbol(symbol)
                .map(this::refreshTokenInfo)
                .orElse(createNewToken(symbol));
    }

    private Optional<TokenWithUsdValue> createNewToken(String symbol) {
        Token newToken = new Token();
        newToken.setSymbol(symbol);
        tokenRepository.persist(newToken);
        return refreshTokenInfo(newToken);
    }

    private TokenWithUsdValue applyCoingeckoFieldsToToken(CoingeckoCoinDetail coin, Token token) {
        if (!coin.name.equals(token.getName())) {
            token.setName(coin.name);
        }

        if (!coin.id.equals(token.getCoinGeckoId())) {
            token.setCoinGeckoId(coin.id);
        }

        if (!coin.image.small.equals(token.getImageSmall())) {
            token.setImageSmall(coin.image.small);
        }

        return new TokenWithUsdValue(token, coin.marketData.currentPrice.usd);
    }

    private Optional<TokenWithUsdValue> refreshTokenInfo(Token token) {
        String symbolToUse = firstNonNull(token.getCoinGeckoSymbol(), token.getSymbol());
        return coingeckoRestClient.getCoins().stream()
                .filter(x -> x.symbol.equalsIgnoreCase(symbolToUse))
                .findFirst()
                .map(x -> applyCoingeckoFieldsToToken(coingeckoRestClient.get(x.id), token));
    }

    private static class TokenWithUsdValue {
        private final Token token;
        private final Float usdValue;

        TokenWithUsdValue(Token token, Float usdValue) {

            this.token = token;
            this.usdValue = usdValue;
        }
    }
}
