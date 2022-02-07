package ch.grignola.service.token;

import ch.grignola.service.token.model.CoingeckoCoin;
import ch.grignola.service.token.model.CoingeckoCoinDetail;
import ch.grignola.service.token.model.CoingeckoCoinMarket;
import ch.grignola.service.token.model.CoingeckoCoinTicker;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.List;

@ApplicationScoped
@RegisterRestClient(baseUri = "https://api.coingecko.com/api/v3/coins")
public interface CoingeckoRestClient {

    @GET
    @Path("/list")
    List<CoingeckoCoin> getCoins();

    @GET
    @Path("/{id}")
    CoingeckoCoinDetail get(@PathParam("id") String id);

    @GET
    @Path("/markets")
    List<CoingeckoCoinMarket> markets(@QueryParam("vs_currency") String currency,
                                      @QueryParam("ids") String ids,
                                      @QueryParam("per_page") String pageSize,
                                      @QueryParam("page") String page,
                                      @QueryParam("sparkline") String sparkline,
                                      @QueryParam("price_change_percentage") String priceChangePercentage);

    @GET
    @Path("/{id}/tickers")
    CoingeckoCoinTicker getTicker(@PathParam("id") String id);

}
