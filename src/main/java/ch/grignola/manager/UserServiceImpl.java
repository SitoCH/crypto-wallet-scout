package ch.grignola.manager;

import ch.grignola.model.User;
import ch.grignola.repository.UserRepository;
import io.quarkus.oidc.IdToken;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    @Inject
    @IdToken
    JsonWebToken idToken;

    @Inject
    UserRepository userRepository;

    public User getLoggedInUser() {
        return userRepository.find("oidcId", idToken.getName())
                .firstResultOptional().orElseGet(() -> {
                    User user = new User();
                    user.setOidcId(idToken.getName());
                    userRepository.persist(user);
                    return user;
                });
    }
}
