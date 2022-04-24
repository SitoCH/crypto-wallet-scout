package ch.grignola.repository;

import ch.grignola.model.AddressFiatLot;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class AddressFiatLotRepository implements PanacheRepository<AddressFiatLot> {

    public List<AddressFiatLot> findByAddress(String address) {
        return list("address", address);
    }

}
