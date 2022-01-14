package ch.grignola.web;

import ch.grignola.service.balance.AddressBalance;
import ch.grignola.service.balance.AddressBalanceChecker;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/api/address/balance")
@Produces("application/json")
public class AddressBalanceResource {

    @Inject
    AddressBalanceChecker addressBalanceChecker;

    @GET
    @Path("/{address}")
    @Transactional
    public AddressBalance getAddressBalance(@PathParam("address") String address) {
        return addressBalanceChecker.getAddressBalance(address);
    }
}
