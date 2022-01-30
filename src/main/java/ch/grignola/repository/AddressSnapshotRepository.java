package ch.grignola.repository;

import ch.grignola.model.AddressSnapshot;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class AddressSnapshotRepository implements PanacheRepository<AddressSnapshot> {

    public void deleteBeforeDateTime(OffsetDateTime discardUntilDateTime) {
        delete("dateTime < ?", discardUntilDateTime);
    }

    public List<AddressSnapshot> findByAddress(String address) {
        return list("address", address);
    }

}
