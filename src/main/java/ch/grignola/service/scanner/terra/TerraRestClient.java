package ch.grignola.service.scanner.terra;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@ApplicationScoped
@RegisterRestClient(baseUri = "https://fcd.terra.dev/v1/bank/")
public interface TerraRestClient {

    @GET
    @Path("{address}")
    TerraBalanceResult getBalance(@PathParam("address") String address);

}
