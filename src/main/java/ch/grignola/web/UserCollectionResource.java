package ch.grignola.web;

import ch.grignola.manager.UserService;
import ch.grignola.model.UserCollection;
import ch.grignola.repository.UserCollectionRepository;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import java.util.List;

@Path("/api/collection")
@Produces("application/json")
public class UserCollectionResource {

    @Inject
    UserCollectionRepository userCollectionRepository;

    @Inject
    UserService userService;

    @GET
    @Transactional
    public List<UserCollectionSummary> getUserCollections() {
        return userCollectionRepository.findByUser(userService.getLoggedInUser()).stream().map(UserCollectionSummary::new).toList();
    }

    @POST
    @Transactional
    @Consumes("application/json")
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

        @JsonProperty("name")
        public String name;

        public UserCollectionSummary(UserCollection userCollection) {
            this.name = userCollection.getName();
        }
    }
}
