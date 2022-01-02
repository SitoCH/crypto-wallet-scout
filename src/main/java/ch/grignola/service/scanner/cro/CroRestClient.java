package ch.grignola.service.scanner.cro;

import ch.grignola.service.scanner.cro.model.CroBalanceResult;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@ApplicationScoped
@RegisterRestClient(baseUri = "https://crypto.org/explorer/api/v1/accounts/")
public interface CroRestClient {

    @GET
    @Path("{address}")
    CroBalanceResult getBalance(@PathParam("address") String address);

}
