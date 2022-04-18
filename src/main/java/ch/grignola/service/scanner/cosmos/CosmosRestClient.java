package ch.grignola.service.scanner.cosmos;

import ch.grignola.service.scanner.cosmos.model.CosmosBalance;
import ch.grignola.service.scanner.cosmos.model.CosmosRewardsBalance;
import ch.grignola.service.scanner.cosmos.model.CosmosStackedBalance;
import ch.grignola.service.scanner.cosmos.model.CosmosUnboundingBalance;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@ApplicationScoped
@RegisterRestClient(baseUri = "https://node.atomscan.com")
public interface CosmosRestClient {

    @GET
    @Path("/staking/delegators/{address}/delegations")
    CosmosStackedBalance getStackedBalance(@PathParam("address") String address);

    @GET
    @Path("/staking/delegators/{address}/unbonding_delegations")
    CosmosUnboundingBalance getUnboundingBalance(@PathParam("address") String address);

    @GET
    @Path("/bank/balances/{address}")
    CosmosBalance getBalance(@PathParam("address") String address);

    @GET
    @Path("/distribution/delegators/{address}/rewards")
    CosmosRewardsBalance getRewardsBalance(@PathParam("address") String address);

}