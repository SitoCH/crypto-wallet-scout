package ch.grignola.repository;

import ch.grignola.model.AddressTokenValue;
import ch.grignola.model.Network;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AddressTokenValueRepository implements PanacheRepository<AddressTokenValue> {

    private static final String QUERY_BY_NETWORK_AND_ADDRESS = "network = ?1 AND address = ?2";

    public Optional<OffsetDateTime> getLastUpdated(Network network, String address) {
        return find(QUERY_BY_NETWORK_AND_ADDRESS, network, address)
                .firstResultOptional()
                .map(AddressTokenValue::getDateTime);
    }

    public void delete(Network network, String address) {
        delete(QUERY_BY_NETWORK_AND_ADDRESS, network, address);
    }

    public List<AddressTokenValue> find(Network network, String address) {
        return list(QUERY_BY_NETWORK_AND_ADDRESS, network, address);
    }
}
