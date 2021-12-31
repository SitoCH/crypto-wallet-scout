package ch.grignola.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/login")
public class LoginResource {

    @GET
    public Response get() throws URISyntaxException {
        return Response.temporaryRedirect(new URI("/dashboard")).build();
    }

}
