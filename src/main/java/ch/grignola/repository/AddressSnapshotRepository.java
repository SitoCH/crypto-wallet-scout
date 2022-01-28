package ch.grignola.repository;

import ch.grignola.model.AddressSnapshot;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AddressSnapshotRepository implements PanacheRepository<AddressSnapshot> {
}
