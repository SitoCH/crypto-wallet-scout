package ch.grignola.service.scanner.terra;

import ch.grignola.service.scanner.terra.model.TerraBalancesResponse;
import ch.grignola.service.scanner.terra.model.TerraContractBalanceResponse;
import ch.grignola.service.scanner.terra.model.TerraRewardsResponse;
import ch.grignola.service.scanner.terra.model.TerraStackingResponse;
import ch.grignola.utils.RestClientResponseFilter;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@ApplicationScoped
@RegisterProvider(RestClientResponseFilter.class)
@RegisterRestClient(baseUri = "https://fcd.terra.dev")
public interface TerraRestClient {

    @GET
    @Path("/cosmos/distribution/v1beta1/delegators/{address}/rewards")
    TerraRewardsResponse getRewards(@PathParam("address") String address);

    @GET
    @Path("/cosmos/bank/v1beta1/balances/{address}")
    TerraBalancesResponse getBalances(@PathParam("address") String address);

    @GET
    @Path("/cosmos/staking/v1beta1/delegations/{address}")
    TerraStackingResponse getStacking(@PathParam("address") String address);

    @GET
    @Path("/terra/wasm/v1beta1/contracts/{contract}/store")
    TerraContractBalanceResponse getContractBalance(@PathParam("contract") String contract,
                                                    @QueryParam("query_msg") String queryMsg);

}
