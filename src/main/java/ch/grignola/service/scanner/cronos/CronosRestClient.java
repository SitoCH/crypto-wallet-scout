package ch.grignola.service.scanner.cronos;

import ch.grignola.service.scanner.cronos.model.CronosBalanceResult;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@ApplicationScoped
@RegisterRestClient(baseUri = "https://crypto.org/explorer/api/v1/accounts/")
public interface CronosRestClient {

    @GET
    @Path("{address}")
    CronosBalanceResult getBalance(@PathParam("address") String address);

}
