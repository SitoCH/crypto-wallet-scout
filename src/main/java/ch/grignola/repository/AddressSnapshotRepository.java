package ch.grignola.repository;

import ch.grignola.model.AddressSnapshot;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;
import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class AddressSnapshotRepository implements PanacheRepository<AddressSnapshot> {

    public void deleteBeforeDateTime(OffsetDateTime discardUntilDateTime) {
        delete("dateTime < ?1", discardUntilDateTime);
    }

    public int pruneOldData(OffsetDateTime pruneBeforeDateTime) {
        Query query = getEntityManager().createNativeQuery("DELETE a FROM AddressSnapshot a LEFT JOIN ( SELECT address, date(dateTime) dateOnly, MAX(ID) max_ID FROM AddressSnapshot GROUP BY address, date(dateTime) ) b ON a.address = b.address AND Date(a.dateTime) = b.dateonly AND a.id = b.max_ID WHERE a.dateTime < ?1 AND b.address IS NULL");
        query.setParameter(1, pruneBeforeDateTime);
        return query.executeUpdate();
    }

    public List<AddressSnapshot> findByAddress(String address) {
        return list("address", address);
    }

}
