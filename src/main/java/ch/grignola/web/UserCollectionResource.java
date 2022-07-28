package ch.grignola.web;

import ch.grignola.model.User;
import ch.grignola.model.UserCollection;
import ch.grignola.model.UserCollectionAddress;
import ch.grignola.repository.UserCollectionRepository;
import ch.grignola.service.UserService;
import ch.grignola.service.balance.AddressBalanceChecker;
import ch.grignola.service.balance.AddressSnapshotService;
import ch.grignola.service.balance.TokenBalances;
import ch.grignola.web.model.HistoricalAddressBalance;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import java.util.List;

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
    AddressSnapshotService addressSnapshotService;

    @Inject
    UserService userService;

    @GET
    @Path("{collectionId}/balance")
    @Transactional
    public TokenBalances getAddressBalance(@PathParam("collectionId") long collectionId) {
        UserCollection userCollection = getUserCollection(collectionId);
        return addressBalanceChecker.getBalances(userCollection.getUserCollectionAddresses().stream().map(UserCollectionAddress::getAddress).toList());
    }

    @GET
    @Path("{collectionId}/balance/history")
    @Transactional
    public HistoricalAddressBalance getHistoricalCollectionBalance(@PathParam("collectionId") long collectionId) {
        UserCollection userCollection = getUserCollection(collectionId);

        HistoricalAddressBalance response = new HistoricalAddressBalance();
        response.snapshots = addressSnapshotService.getHistoricalAddressesBalance(userCollection.getUserCollectionAddresses()
                .stream()
                .map(UserCollectionAddress::getAddress)
                .toList());

        return response;
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
