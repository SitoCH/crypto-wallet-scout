package ch.grignola.service.token;

import ch.grignola.model.Network;
import ch.grignola.model.Token;
import ch.grignola.repository.TokenRepository;
import ch.grignola.service.token.model.CoingeckoCoin;
import ch.grignola.service.token.model.CoingeckoCoinDetail;
import ch.grignola.service.token.model.CoingeckoCoinMarket;
import ch.grignola.service.token.model.TokenDetail;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ch.grignola.model.Network.OPTIMISM;
import static ch.grignola.model.Network.POLYGON;
import static ch.grignola.service.token.TokenContractStatus.*;
import static java.lang.String.join;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static java.util.Comparator.comparing;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@ApplicationScoped
public class TokenProviderImpl implements TokenProvider {
    private static final Logger LOG = Logger.getLogger(TokenProviderImpl.class);
    private final RateLimiter rateLimiter;
    @Inject
    @CacheName("token-provider-cache")
    Cache cache;
    @Inject
    TokenRepository tokenRepository;
    @Inject
    @RestClient
    CoingeckoRestClient coingeckoRestClient;

    TokenProviderImpl() {
        rateLimiter = RateLimiterRegistry.of(RateLimiterConfig.custom()
                .timeoutDuration(ofSeconds(10))
                .limitRefreshPeriod(ofMillis(1500))
                .limitForPeriod(8)
                .build()).rateLimiter("TokenProvider");
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

        return Optional.of(new TokenDetail(token.getId().toString(), token.getParentId(), token.getName(), coin.image, token.getSymbol(),
                coin.usdValue, token.getAllocation(), coin.priceChangePercentage24h, coin.priceChangePercentage7d,
                coin.priceChangePercentage30d, coin.priceChangePercentage200d));
    }

    private CachedCoinDetail getCoingeckoCoin(String coinGeckoId) {
        return cache.get(coinGeckoId, key -> {
            rateLimiter.acquirePermission();
            LOG.infof("Load %s from Coingecko", key);
            return new CachedCoinDetail(coingeckoRestClient.get(key));
        }).await().indefinitely();
    }

    private Optional<TokenDetail> getInfoFromCoingecko(Token token) {
        List<CoingeckoCoin> coins = getCoingeckoCoinList();
        return coins.stream()
                .filter(x -> isNotBlank(token.getCoinGeckoId()) && x.id.equalsIgnoreCase(token.getCoinGeckoId()))
                .findFirst()
                .or(() -> coins.stream()
                        .sorted(comparing(x -> x.name.length()))
                        .filter(x -> x.symbol.equalsIgnoreCase(token.getSymbol()))
                        .findFirst())
                .flatMap(x -> applyCoingeckoFieldsToToken(x.id, token));
    }

    private List<CoingeckoCoin> getCoingeckoCoinList() {
        return cache.get("full-coins-list", key -> {
            LOG.info("Load coins list from Coingecko");
            rateLimiter.acquirePermission();
            return coingeckoRestClient.getCoins();
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
        rateLimiter.acquirePermission();
        return coingeckoRestClient.markets("usd", coinGeckoIds, "1000", "1", "false", "24h,7d,30d,200d")
                .stream().map(CachedCoinDetail::new).toList();
    }

    @Override
    public void refreshCache() {
        cache.invalidateAll().await().indefinitely();
        List<String> ids = tokenRepository.streamAll()
                .map(Token::getCoinGeckoId)
                .filter(Objects::nonNull).toList();
        String idsToFetch = join(",", ids);
        List<CoingeckoCoin> coins = getCoingeckoCoinList();
        LOG.infof("Token cache list refreshed, loaded %s coins", coins.size());
        List<String> refreshedTokens = new ArrayList<>();
        getCoingeckoCoins(idsToFetch).forEach(x -> {
            cache.get(x.id, key -> x).await().indefinitely();
            refreshedTokens.add(x.name);
        });
        LOG.infof("Token cache refreshed for %s", join(", ", refreshedTokens));
    }

    private Optional<String> getPlatformId(Network network) {
        if (network == POLYGON) {
            return of("polygon-pos");
        }

        if (network == OPTIMISM) {
            return of("optimistic-ethereum");
        }

        return empty();
    }

    public TokenContract getContract(Network network, String contractAddress) {
        try {
            rateLimiter.acquirePermission();
            return getPlatformId(network)
                    .map(platformId -> coingeckoRestClient.getContract(platformId, contractAddress))
                    .map(contract -> new TokenContract(contract.name, contract.liquidityScore == 0 ? BANNED : VERIFIED))
                    .orElse(new TokenContract("", PLATFORM_NOT_FOUND));
        } catch (WebApplicationException e) {
            return new TokenContract("", CONTRACT_NOT_FOUND);
        }
    }

    private static class CachedCoinDetail {
        String name;
        String id;
        String image;
        float priceChangePercentage24h;
        float priceChangePercentage7d;
        float priceChangePercentage30d;
        float priceChangePercentage200d;
        float usdValue;


        CachedCoinDetail(CoingeckoCoinDetail coingeckoCoinDetail) {
            this.id = coingeckoCoinDetail.id;
            this.name = coingeckoCoinDetail.name;
            this.image = coingeckoCoinDetail.image.large;
            this.usdValue = coingeckoCoinDetail.marketData.currentPrice.usd;
            this.priceChangePercentage24h = coingeckoCoinDetail.marketData.priceChange24h;
            this.priceChangePercentage7d = coingeckoCoinDetail.marketData.priceChangePercentage7d;
            this.priceChangePercentage30d = coingeckoCoinDetail.marketData.priceChangePercentage30d;
            this.priceChangePercentage200d = coingeckoCoinDetail.marketData.priceChangePercentage200d;
        }

        CachedCoinDetail(CoingeckoCoinMarket coingeckoCoinMarket) {
            this.id = coingeckoCoinMarket.id;
            this.name = coingeckoCoinMarket.name;
            this.image = coingeckoCoinMarket.image;
            this.usdValue = coingeckoCoinMarket.currentPrice;
            this.priceChangePercentage24h = coingeckoCoinMarket.priceChangePercentage24hInCurrency;
            this.priceChangePercentage7d = coingeckoCoinMarket.priceChangePercentage7dInCurrency;
            this.priceChangePercentage30d = coingeckoCoinMarket.priceChangePercentage30dInCurrency;
            this.priceChangePercentage200d = coingeckoCoinMarket.priceChangePercentage200dInCurrency;
        }
    }
}
