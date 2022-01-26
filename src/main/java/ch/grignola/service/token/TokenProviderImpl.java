package ch.grignola.service.token;

import ch.grignola.model.Allocation;
import ch.grignola.model.Token;
import ch.grignola.repository.TokenRepository;
import ch.grignola.service.token.model.CoingeckoCoin;
import ch.grignola.service.token.model.CoingeckoCoinDetail;
import ch.grignola.service.token.model.TokenDetail;
import io.github.bucket4j.BlockingBucket;
import io.github.bucket4j.Bucket;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static io.github.bucket4j.Bandwidth.classic;
import static io.github.bucket4j.Refill.intervally;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@ApplicationScoped
public class TokenProviderImpl implements TokenProvider {

    private static final Logger LOG = Logger.getLogger(TokenProviderImpl.class);

    @Inject
    TokenRepository tokenRepository;

    @Inject
    @RestClient
    CoingeckoRestClient coingeckoRestClient;

    private final BlockingBucket bucket;

    private TokenProviderImpl() {
        bucket = Bucket.builder()
                .addLimit(classic(50, intervally(50, ofMinutes(1))))
                .build().asBlocking();
    }

    private Token createNewToken(String symbol) {
        Token newToken = new Token();
        newToken.setSymbol(symbol);
        tokenRepository.persist(newToken);
        return newToken;
    }

    private Optional<TokenDetail> applyCoingeckoFieldsToToken(String coinGeckoId, Token token) {

        try {
            bucket.consume(1);
            CoingeckoCoinDetail coin = coingeckoRestClient.get(coinGeckoId);
            if (!coin.name.equals(token.getName())) {
                token.setName(coin.name);
            }

            if (!coin.id.equals(token.getCoinGeckoId())) {
                token.setCoinGeckoId(coin.id);
            }

            Allocation defaultAllocation = null;
            if (coin.categories.contains("Aave Tokens")) {
                defaultAllocation = Allocation.STACKED;
            }

            return Optional.of(new TokenDetail(token.getId().toString(), token.getName(), coin.image.small, token.getSymbol(),
                    coin.marketData.currentPrice.usd, defaultAllocation, coin.marketData.priceChangePercentage24h, coin.marketData.priceChangePercentage7d));
        } catch (InterruptedException e) {
            LOG.infof("Unable to load coin %s from Coingecko", coinGeckoId);
            Thread.currentThread().interrupt();
            return Optional.empty();
        }

    }

    private Optional<TokenDetail> getInfoFromCoingecko(Token token) {
        if (token.isExcludeFromBalance()) {
            return Optional.empty();
        }

        try {
            bucket.consume(1);
        } catch (InterruptedException e) {
            LOG.infof("InterruptedException while loading token %s from Coingecko", token.getSymbol());
            Thread.currentThread().interrupt();
            return Optional.empty();
        }

        List<CoingeckoCoin> coins = coingeckoRestClient.getCoins();
        return coins.stream()
                .filter(x -> isNotBlank(token.getCoinGeckoId()) && x.id.equalsIgnoreCase(token.getCoinGeckoId()))
                .findFirst()
                .or(() -> coins.stream()
                        .filter(x -> x.symbol.equalsIgnoreCase(token.getSymbol()))
                        .findFirst())
                .flatMap(x -> applyCoingeckoFieldsToToken(x.id, token));
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
