package ch.grignola.repository;

import ch.grignola.model.AddressSnapshot;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.time.OffsetDateTime;

@ApplicationScoped
public class AddressSnapshotRepository implements PanacheRepository<AddressSnapshot> {

    public void deleteBeforeDateTime(OffsetDateTime discardUntilDateTime) {
        delete("dateTime < ?", discardUntilDateTime);
    }

}
