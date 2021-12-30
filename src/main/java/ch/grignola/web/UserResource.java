package ch.grignola.web;

import ch.grignola.manager.UserManager;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.commons.codec.digest.Crypt;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/user")
public class UserResource {

    @Inject
    UserManager userManager;

    @GET
    @Transactional
    public UserProfile getUser() {
        return new UserProfile(Crypt.crypt(userManager.getLoggedInUser().getId().toString()));
    }

    @RegisterForReflection
    static class UserProfile {

        private final String hashedId;

        public UserProfile(String hashedId) {

            this.hashedId = hashedId;
        }

        public String getHashedId() {
            return hashedId;
        }
    }
}
