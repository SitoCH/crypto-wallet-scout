package ch.grignola.repository;

import ch.grignola.model.Network;
import ch.grignola.model.TerraTokenContract;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Stream;

@ApplicationScoped
public class TerraTokenContractRepository implements PanacheRepository<TerraTokenContract> {

    public Stream<TerraTokenContract> findByNetwork(Network network) {
        return stream("network", network);
    }
}
