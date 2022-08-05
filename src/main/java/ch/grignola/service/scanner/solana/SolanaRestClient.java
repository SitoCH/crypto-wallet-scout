package ch.grignola.service.scanner.solana;

import ch.grignola.service.scanner.solana.model.SolanaNativeBalance;
import ch.grignola.service.scanner.solana.model.SolanaTokenBalance;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.List;

@ApplicationScoped
@RegisterRestClient(baseUri = "https://public-api.solscan.io/account/", configKey = "solana")
public interface SolanaRestClient {

    @GET
    @Path("/tokens")
    List<SolanaTokenBalance> getAccountTokens(@QueryParam("account") String account);

    @GET
    @Path("{account}")
    SolanaNativeBalance getNativeBalance(@PathParam("account") String account);
}
