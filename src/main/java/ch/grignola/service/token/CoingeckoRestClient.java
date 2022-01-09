package ch.grignola.service.token;

import ch.grignola.service.token.model.CoingeckoCoin;
import ch.grignola.service.token.model.CoingeckoCoinDetail;
import ch.grignola.service.token.model.CoingeckoCoinTicker;
import io.quarkus.cache.CacheResult;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@ApplicationScoped
@RegisterRestClient(baseUri = "https://api.coingecko.com/api/v3/coins/")
public interface CoingeckoRestClient {

    @GET
    @Path("/list")
    @CacheResult(cacheName = "coingecko-coins-cache")
    List<CoingeckoCoin> getCoins();

    @GET
    @Path("/{id}")
    @CacheResult(cacheName = "coingecko-coins-detail-cache")
    CoingeckoCoinDetail get(@PathParam("id") String id);

    @GET
    @Path("/{id}/tickers")
    @CacheResult(cacheName = "coingecko-coins-ticker-cache")
    CoingeckoCoinTicker getTicker(@PathParam("id") String id);

}
