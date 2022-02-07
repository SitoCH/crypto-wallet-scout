package ch.grignola.repository;

import ch.grignola.model.BannedContract;
import ch.grignola.model.Network;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@ApplicationScoped
public class BannedContractRepository implements PanacheRepository<BannedContract> {

    public List<BannedContract> findByNetwork(Network network) {
        return list("network", network);
    }

    public Map<Network, List<BannedContract>> findAllByNetwork() {
        return listAll()
                .stream()
                .collect(groupingBy(BannedContract::getNetwork));
    }
}
