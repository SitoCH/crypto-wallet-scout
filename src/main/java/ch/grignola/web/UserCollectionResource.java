package ch.grignola.web;

import ch.grignola.model.User;
import ch.grignola.model.UserCollection;
import ch.grignola.model.UserCollectionAddress;
import ch.grignola.repository.AddressSnapshotRepository;
import ch.grignola.repository.UserCollectionRepository;
import ch.grignola.service.UserService;
import ch.grignola.service.balance.AddressBalance;
import ch.grignola.service.balance.AddressBalanceChecker;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/api/collection")
@Produces("application/json")
@Consumes("application/json")
public class UserCollectionResource {

    private static final Logger LOG = Logger.getLogger(UserCollectionResource.class);

    @Inject
    UserCollectionRepository userCollectionRepository;

    @Inject
    AddressBalanceChecker addressBalanceChecker;

    @Inject
    AddressSnapshotRepository addressSnapshotRepository;

    @Inject
    UserService userService;

    @GET
    @Path("{collectionId}/balance")
    @Transactional
    public List<AddressBalance> getAddressBalance(@PathParam("collectionId") long collectionId) {
        UserCollection userCollection = getUserCollection(collectionId);
        return userCollection.getUserCollectionAddresses().stream().map(x -> addressBalanceChecker.getAddressBalance(x.getAddress())).toList();
    }

    @GET
    @Path("{collectionId}/balance/history")
    @Transactional
    public HistoricalAddressBalance getHistoricalAddressBalance(@PathParam("collectionId") long collectionId) {
        UserCollection userCollection = getUserCollection(collectionId);

        HistoricalAddressBalance response = new HistoricalAddressBalance();
        response.snapshots = new HashMap<>();

        userCollection.getUserCollectionAddresses()
                .forEach(userCollectionAddresses -> addSnapshot(response.snapshots, userCollectionAddresses));

        return response;
    }

    private void addSnapshot(Map<OffsetDateTime, BigDecimal> snapshots, UserCollectionAddress userCollectionAddresses) {
        addressSnapshotRepository.findByAddress(userCollectionAddresses.getAddress())
                .forEach(addressSnapshot -> {
                    OffsetDateTime key = addressSnapshot.getDateTime().truncatedTo(ChronoUnit.HOURS);
                    snapshots.merge(key, addressSnapshot.getUsdValue(), BigDecimal::add);
                });
    }

    private UserCollection getUserCollection(long collectionId) {
        UserCollection userCollection = userCollectionRepository.findById(collectionId);
        if (!userCollection.getUser().getId().equals(userService.getLoggedInUser().getId())) {
            throw new BadRequestException();
        }
        return userCollection;
    }

    @GET
    @Transactional
    public List<UserCollectionSummary> getUserCollections() {
        return userCollectionRepository.findByUser(userService.getLoggedInUser()).stream().map(UserCollectionSummary::new).toList();
    }

    @POST
    @Path("{collectionId}/add/{address}")
    @Transactional
    public void addAddress(@PathParam("collectionId") long collectionId,
                           @PathParam("address") String address) {

        User user = userService.getLoggedInUser();

        userCollectionRepository.findByIdOptional(collectionId)
                .ifPresent(collection -> {
                    if (collection.getUser().getOidcId().equals(user.getOidcId()) && collection.getUserCollectionAddresses().stream().noneMatch(x -> x.getAddress().equals(address))) {
                        UserCollectionAddress userCollectionAddress = new UserCollectionAddress();
                        userCollectionAddress.setAddress(address);
                        collection.addUserCollectionAddresses(userCollectionAddress);
                        userCollectionRepository.persist(collection);
                        LOG.infof("User %s added address %s to collection %s ",
                                user.getId(), address, collection.getId());
                    } else {
                        LOG.warnf("User %s tried to add address %s to collection %s without permission",
                                user.getId(), address, collection.getId());
                    }
                });
    }

    @POST
    @Transactional
    public UserCollectionSummary newUserCollection(NewUserCollection newUserCollection) {
        UserCollection userCollection = new UserCollection();
        userCollection.setUser(userService.getLoggedInUser());
        userCollection.setName(newUserCollection.name);
        userCollectionRepository.persist(userCollection);
        return new UserCollectionSummary(userCollection);
    }

    public static class HistoricalAddressBalance {
        @JsonProperty("snapshots")
        public Map<OffsetDateTime, BigDecimal> snapshots;
    }

    public static class NewUserCollection {
        @JsonProperty("name")
        public String name;
    }

    public static class UserCollectionSummary {

        @JsonProperty("id")
        public long id;

        @JsonProperty("name")
        public String name;

        @JsonProperty("addresses")
        public List<String> addresses;

        public UserCollectionSummary(UserCollection userCollection) {
            this.id = userCollection.getId();
            this.name = userCollection.getName();
            this.addresses = userCollection.getUserCollectionAddresses().stream()
                    .map(UserCollectionAddress::getAddress)
                    .sorted().toList();
        }
    }
}
