package ch.grignola.repository;

import ch.grignola.model.User;
import ch.grignola.model.UserCollection;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class UserCollectionRepository implements PanacheRepository<UserCollection> {

    public List<UserCollection> findByUser(User user) {
        return find("user", user).stream().toList();
    }
}
