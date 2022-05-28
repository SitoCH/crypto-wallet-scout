package ch.grignola.service.scanner.terra.client;

import ch.grignola.service.scanner.terra.model.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

public interface TerraCommonRestClient {
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
    @Path("/cosmos/staking/v1beta1/delegators/{address}/unbonding_delegations")
    TerraUnbondingResponse getUnbonding(@PathParam("address") String address);

    @GET
    @Path("/terra/wasm/v1beta1/contracts/{contract}/store")
    TerraContractBalanceResponse getContractBalance(@PathParam("contract") String contract,
                                                    @QueryParam("query_msg") String queryMsg);
}
