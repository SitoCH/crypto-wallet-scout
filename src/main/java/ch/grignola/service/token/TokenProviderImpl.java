package ch.grignola.service.token;

import ch.grignola.model.Token;
import ch.grignola.repository.TokenRepository;
import ch.grignola.service.token.model.CoingeckoCoin;
import ch.grignola.service.token.model.CoingeckoCoinDetail;
import ch.grignola.service.token.model.CoingeckoCoinMarket;
import ch.grignola.service.token.model.TokenDetail;
import io.github.bucket4j.BlockingBucket;
import io.github.bucket4j.Bucket;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static io.github.bucket4j.Bandwidth.classic;
import static io.github.bucket4j.Refill.intervally;
import static java.time.Duration.ofMillis;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@ApplicationScoped
public class TokenProviderImpl implements TokenProvider {

    private static final Logger LOG = Logger.getLogger(TokenProviderImpl.class);

    @Inject
    @CacheName("token-provider-cache")
    Cache cache;

    @Inject
    TokenRepository tokenRepository;

    @Inject
    @RestClient
    CoingeckoRestClient coingeckoRestClient;

    private final BlockingBucket bucket;

    private TokenProviderImpl() {
        bucket = Bucket.builder()
                .addLimit(classic(8, intervally(8, ofMillis(1500))))
                .build().asBlocking();
    }

    private Token createNewToken(String symbol) {
        Token newToken = new Token();
        newToken.setSymbol(symbol);
        tokenRepository.persistAndFlush(newToken);
        LOG.infof("Created new token %s with id %s", newToken.getSymbol(), newToken.getId());
        return newToken;
    }

    private Optional<TokenDetail> applyCoingeckoFieldsToToken(String coinGeckoId, Token token) {
        CachedCoinDetail coin = getCoingeckoCoin(coinGeckoId);

        if (!coin.name.equals(token.getName())) {
            token.setName(coin.name);
        }

        if (!coin.id.equals(token.getCoinGeckoId())) {
            token.setCoinGeckoId(coin.id);
        }

        return Optional.of(new TokenDetail(token.getId().toString(), token.getName(), coin.image, token.getSymbol(),
                coin.usdValue, token.getAllocation(), coin.priceChangePercentage24h, coin.priceChangePercentage7d));
    }

    private CachedCoinDetail getCoingeckoCoin(String coinGeckoId) {
        return cache.get(coinGeckoId, key -> {
            try {
                bucket.consume(1);
                LOG.infof("Load %s from Coingecko", key);
                return new CachedCoinDetail(coingeckoRestClient.get(key));
            } catch (InterruptedException e) {
                LOG.warnf("Unable to load coin %s from Coingecko", coinGeckoId);
                Thread.currentThread().interrupt();
                return null;
            }
        }).await().indefinitely();
    }

    private Optional<TokenDetail> getInfoFromCoingecko(Token token) {
        if (token.isExcludeFromBalance()) {
            return Optional.empty();
        }

        List<CoingeckoCoin> coins = getCoingeckoCoinList();
        return coins.stream()
                .filter(x -> isNotBlank(token.getCoinGeckoId()) && x.id.equalsIgnoreCase(token.getCoinGeckoId()))
                .findFirst()
                .or(() -> coins.stream()
                        .filter(x -> x.symbol.equalsIgnoreCase(token.getSymbol()))
                        .findFirst())
                .flatMap(x -> applyCoingeckoFieldsToToken(x.id, token));
    }

    private List<CoingeckoCoin> getCoingeckoCoinList() {
        return cache.get("full-coins-list", key -> {
            try {
                LOG.info("Load coins list from Coingecko");
                bucket.consume(1);
                return coingeckoRestClient.getCoins();
            } catch (InterruptedException e) {
                LOG.warnf("Unable to load coins from Coingecko");
                Thread.currentThread().interrupt();
                return null;
            }
        }).await().indefinitely();
    }

    @Override
    public Optional<TokenDetail> getBySymbol(String symbol) {
        return getInfoFromCoingecko(tokenRepository.findBySymbol(symbol).orElseGet(() -> createNewToken(symbol)));
    }

    @Override
    public Optional<TokenDetail> getById(String tokenId) {
        return tokenRepository.findByIdOptional(Long.parseLong(tokenId)).flatMap(this::getInfoFromCoingecko);
    }

    private List<CachedCoinDetail> getCoingeckoCoins(String coinGeckoIds) {
        try {
            bucket.consume(1);
            return coingeckoRestClient.markets("usd", coinGeckoIds, "1000", "1", "false", "24h,7d")
                    .stream().map(CachedCoinDetail::new).toList();
        } catch (InterruptedException e) {
            LOG.warnf("Unable to load coins %s from Coingecko", coinGeckoIds);
            Thread.currentThread().interrupt();
            return emptyList();
        }
    }

    @Override
    public void refreshCache() {
        List<String> ids = tokenRepository.findAll().stream()
                .map(Token::getCoinGeckoId)
                .filter(Objects::nonNull).toList();
        String idsToFetch = String.join(",", ids);
        cache.invalidateAll().await().indefinitely();
        getCoingeckoCoinList();
        getCoingeckoCoins(idsToFetch).forEach(x -> {
            cache.get(x.id, key -> x).await().indefinitely();
            LOG.infof("Token cache refreshed for %s", x.name);
        });
    }

    private static class CachedCoinDetail {
        String name;
        String id;
        String image;
        float priceChangePercentage24h;
        float priceChangePercentage7d;
        float usdValue;

        CachedCoinDetail(CoingeckoCoinDetail coingeckoCoinDetail) {
            this.id = coingeckoCoinDetail.id;
            this.name = coingeckoCoinDetail.name;
            this.image = coingeckoCoinDetail.image.large;
            this.usdValue = coingeckoCoinDetail.marketData.currentPrice.usd;
            this.priceChangePercentage24h = coingeckoCoinDetail.marketData.priceChange24h;
            this.priceChangePercentage7d = coingeckoCoinDetail.marketData.priceChangePercentage7d;
        }

        CachedCoinDetail(CoingeckoCoinMarket coingeckoCoinMarket) {
            this.id = coingeckoCoinMarket.id;
            this.name = coingeckoCoinMarket.name;
            this.image = coingeckoCoinMarket.image;
            this.usdValue = coingeckoCoinMarket.currentPrice;
            this.priceChangePercentage24h = coingeckoCoinMarket.priceChangePercentage24hInCurrency;
            this.priceChangePercentage7d = coingeckoCoinMarket.priceChangePercentage7dInCurrency;
        }
    }
}
