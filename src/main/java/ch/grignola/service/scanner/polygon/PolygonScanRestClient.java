package ch.grignola.service.scanner.polygon;

import ch.grignola.service.scanner.etherscan.model.EthereumTokenBalanceResult;
import ch.grignola.service.scanner.etherscan.model.EthereumTokenEventsResult;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;

@ApplicationScoped
@RegisterRestClient(baseUri = "https://api.polygonscan.com/api?module=account&tag=latest")
public interface PolygonScanRestClient {
    @GET
    EthereumTokenEventsResult getTokenEvents(@QueryParam("apikey") String apikey,
                                             @QueryParam("action") String action,
                                             @QueryParam("address") String address);

    @GET
    EthereumTokenBalanceResult getTokenBalance(@QueryParam("apikey") String apikey,
                                               @QueryParam("action") String action,
                                               @QueryParam("address") String address,
                                               @QueryParam("contractaddress") String contractAddress);

    @GET
    EthereumTokenBalanceResult getBalance(@QueryParam("apikey") String apikey,
                                          @QueryParam("action") String action,
                                          @QueryParam("address") String address);
}
