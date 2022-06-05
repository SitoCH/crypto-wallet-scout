package ch.grignola.repository;

import ch.grignola.model.ContractVerificationStatus;
import ch.grignola.model.Network;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ContractVerificationStatusRepository implements PanacheRepository<ContractVerificationStatus> {

    public List<ContractVerificationStatus> findByNetwork(Network network) {
        return list("network", network);
    }

    public List<ContractVerificationStatus> findByStatus(ContractVerificationStatus.Status status) {
        return list("status", status);
    }

}
