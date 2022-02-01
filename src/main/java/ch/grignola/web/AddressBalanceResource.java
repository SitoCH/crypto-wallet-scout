package ch.grignola.web;

import ch.grignola.repository.AddressSnapshotRepository;
import ch.grignola.service.balance.AddressBalance;
import ch.grignola.service.balance.AddressBalanceChecker;
import ch.grignola.web.model.HistoricalAddressBalance;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

@Path("/api/address/balance")
@Produces("application/json")
public class AddressBalanceResource {

    @Inject
    AddressBalanceChecker addressBalanceChecker;

    @Inject
    AddressSnapshotRepository addressSnapshotRepository;

    @GET
    @Path("/{address}")
    @Transactional
    public AddressBalance getAddressBalance(@PathParam("address") String address) {
        return addressBalanceChecker.getAddressBalance(address);
    }

    @GET
    @Path("{address}/balance/history")
    @Transactional
    public HistoricalAddressBalance getHistoricalAddressBalance(@PathParam("address") String address) {
        HistoricalAddressBalance response = new HistoricalAddressBalance();
        response.snapshots = new HashMap<>();
        addressSnapshotRepository.findByAddress(address)
                .forEach(addressSnapshot -> {
                    OffsetDateTime key = addressSnapshot.getDateTime().truncatedTo(ChronoUnit.HOURS);
                    response.snapshots.merge(key, addressSnapshot.getUsdValue(), BigDecimal::add);
                });
        return response;
    }
}
