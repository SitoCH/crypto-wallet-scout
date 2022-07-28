package ch.grignola.service.balance;

import ch.grignola.repository.AddressSnapshotRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class AddressSnapshotServiceImpl implements AddressSnapshotService {

    @Inject
    AddressSnapshotRepository addressSnapshotRepository;

    public Map<OffsetDateTime, BigDecimal> getHistoricalAddressBalance(String address) {
        Map<OffsetDateTime, BigDecimal> snapshots = new HashMap<>();
        addSnapshot(snapshots, address);
        return snapshots;
    }

    @Override
    public Map<OffsetDateTime, BigDecimal> getHistoricalAddressesBalance(List<String> addresses) {
        Map<OffsetDateTime, BigDecimal> snapshots = new HashMap<>();
        addresses.forEach(address -> addSnapshot(snapshots, address));
        return snapshots;
    }

    private void addSnapshot(Map<OffsetDateTime, BigDecimal> snapshots, String address) {
        addressSnapshotRepository.findByAddress(address)
                .forEach(addressSnapshot -> {
                    OffsetDateTime key = addressSnapshot.getDateTime().truncatedTo(ChronoUnit.HOURS);
                    snapshots.merge(key, addressSnapshot.getUsdValue(), BigDecimal::add);
                });
    }

}
