package ch.grignola.service.scanner.polkadot;

import ch.grignola.service.scanner.polkadot.common.PolkadotBalanceResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@ApplicationScoped
@RegisterRestClient(baseUri = "https://api.dotscanner.com", configKey = "polkadot")
public interface PolkadotRestClient {

    @GET
    @Path("/accounts/{address}")
    PolkadotBalanceResponse getBalance(@PathParam("address") String address,
                                       @QueryParam("chain") String chain);

}
