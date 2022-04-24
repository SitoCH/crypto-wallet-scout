package ch.grignola.web;

import ch.grignola.service.balance.AddressBalanceChecker;
import ch.grignola.service.balance.AddressSnapshotService;
import ch.grignola.service.balance.TokenBalance;
import ch.grignola.web.model.HistoricalAddressBalance;
import ch.grignola.web.model.HistoricalAddressBalanceWithLots;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/api/address/balance")
@Produces("application/json")
public class AddressBalanceResource {

    @Inject
    AddressBalanceChecker addressBalanceChecker;

    @Inject
    AddressSnapshotService addressSnapshotService;

    @GET
    @Path("/{address}")
    @Transactional
    public List<TokenBalance> getAddressBalance(@PathParam("address") String address) {
        return addressBalanceChecker.getBalance(address);
    }

    @GET
    @Path("{address}/balance/history")
    @Transactional
    public HistoricalAddressBalance getHistoricalAddressBalance(@PathParam("address") String address) {
        HistoricalAddressBalance response = new HistoricalAddressBalance();
        response.snapshots = addressSnapshotService.getHistoricalAddressBalance(address);
        return response;
    }

    @GET
    @Path("{address}/balance/history/with-lots")
    @Transactional
    public HistoricalAddressBalanceWithLots getHistoricalAddressBalanceWithLots(@PathParam("address") String address) {
        HistoricalAddressBalanceWithLots response = new HistoricalAddressBalanceWithLots();
        response.snapshots = addressSnapshotService.getHistoricalAddressBalanceWithFiatLots(address);
        return response;
    }
}
