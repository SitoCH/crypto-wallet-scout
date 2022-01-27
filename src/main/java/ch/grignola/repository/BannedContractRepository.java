package ch.grignola.repository;

import ch.grignola.model.BannedContract;
import ch.grignola.model.Network;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class BannedContractRepository implements PanacheRepository<BannedContract> {

    public List<BannedContract> findByNetwork(Network network) {
        return list("network", network);
    }
}
