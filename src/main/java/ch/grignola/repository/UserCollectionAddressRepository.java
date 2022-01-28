package ch.grignola.repository;

import ch.grignola.model.UserCollectionAddress;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserCollectionAddressRepository implements PanacheRepository<UserCollectionAddress> {

}
