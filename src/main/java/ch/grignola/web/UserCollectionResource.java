package ch.grignola.web;

import ch.grignola.service.scanner.AddressBalance;

import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/api/collection")
@Produces("application/json")
public class UserCollectionResource {

    @GET
    @Path("/{address}")
    @Transactional
    public AddressBalance getAddressBalance(@PathParam("address") String address) {
        return null;
    }
}
