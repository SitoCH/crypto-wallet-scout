package ch.grignola.service;

import ch.grignola.model.User;
import ch.grignola.repository.UserRepository;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    @Inject
    JsonWebToken token;

    @Inject
    UserRepository userRepository;

    public User getLoggedInUser() {
        return userRepository.find("oidcId", token.getSubject())
                .firstResultOptional().orElseGet(() -> {
                    User user = new User();
                    user.setOidcId(token.getSubject());
                    userRepository.persist(user);
                    return user;
                });
    }
}
