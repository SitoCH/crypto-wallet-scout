package ch.grignola.service.token;

import ch.grignola.model.Token;
import ch.grignola.repository.TokenRepository;
import ch.grignola.service.token.model.CoingeckoCoinDetail;
import ch.grignola.service.token.model.TokenDetail;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

@ApplicationScoped
public class TokenProviderImpl implements TokenProvider {

    @Inject
    TokenRepository tokenRepository;

    @Inject
    @RestClient
    CoingeckoRestClient coingeckoRestClient;

    private Token createNewToken(String symbol) {
        Token newToken = new Token();
        newToken.setSymbol(symbol);
        tokenRepository.persist(newToken);
        return newToken;
    }

    private TokenDetail applyCoingeckoFieldsToToken(CoingeckoCoinDetail coin, Token token) {
        if (!coin.name.equals(token.getName())) {
            token.setName(coin.name);
        }

        return new TokenDetail(token.getId().toString(), token.getName(), coin.image.small, token.getSymbol(),
                coin.marketData.currentPrice.usd, coin.marketData.priceChange24h, coin.marketData.priceChangePercentage7d);
    }

    private Optional<TokenDetail> getInfoFromCoingecko(Token token) {
        String symbolToUse = firstNonNull(token.getCoinGeckoSymbol(), token.getSymbol());
        return coingeckoRestClient.getCoins().stream()
                .filter(x -> x.symbol.equalsIgnoreCase(symbolToUse))
                .findFirst()
                .map(x -> applyCoingeckoFieldsToToken(coingeckoRestClient.get(x.id), token));
    }

    @Override
    public Optional<TokenDetail> getBySymbol(String symbol) {
        return getInfoFromCoingecko(tokenRepository.findBySymbol(symbol).orElseGet(() -> createNewToken(symbol)));
    }

    @Override
    public Optional<TokenDetail> getById(String tokenId) {
        return tokenRepository.findByIdOptional(Long.parseLong(tokenId)).flatMap(this::getInfoFromCoingecko);
    }
}
