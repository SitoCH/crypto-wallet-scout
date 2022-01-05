package ch.grignola.service.scanner.terra;

import ch.grignola.service.scanner.terra.model.TerraBalanceResult;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@ApplicationScoped
@RegisterRestClient(baseUri = "https://fcd.terra.dev/v1/staking/")
public interface TerraRestClient {

    @GET
    @Path("{address}")
    TerraBalanceResult getBalance(@PathParam("address") String address);

}
