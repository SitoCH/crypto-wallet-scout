package ch.grignola.repository;

import ch.grignola.model.TerraTokenContract;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TerraTokenContractRepository implements PanacheRepository<TerraTokenContract> {

}
