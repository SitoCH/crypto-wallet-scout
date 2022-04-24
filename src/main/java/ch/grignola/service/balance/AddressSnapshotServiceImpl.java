package ch.grignola.service.balance;

import ch.grignola.model.AddressFiatLot;
import ch.grignola.repository.AddressFiatLotRepository;
import ch.grignola.repository.AddressSnapshotRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ZERO;

@ApplicationScoped
public class AddressSnapshotServiceImpl implements AddressSnapshotService {

    @Inject
    AddressSnapshotRepository addressSnapshotRepository;

    @Inject
    AddressFiatLotRepository addressFiatLotRepository;

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

    public Map<LocalDate, BigDecimal> getHistoricalAddressesBalanceWithFiatLots(List<String> addresses) {
        Map<LocalDate, BigDecimal> snapshots = new HashMap<>();

        addresses.forEach(address -> addSnapshotsWithLots(address, snapshots));

        return snapshots;
    }

    public Map<LocalDate, BigDecimal> getHistoricalAddressBalanceWithFiatLots(String address) {
        Map<LocalDate, BigDecimal> snapshots = new HashMap<>();

        addSnapshotsWithLots(address, snapshots);

        return snapshots;
    }

    private void addSnapshotsWithLots(String address, Map<LocalDate, BigDecimal> snapshots) {
        List<AddressFiatLot> lots = addressFiatLotRepository.findByAddress(address);

        addressSnapshotRepository.findLastDailySnapshotByAddress(address)
                .forEach(addressSnapshot -> {
                    LocalDate key = addressSnapshot.getDateTime().toLocalDate();
                    BigDecimal usdValue = addressSnapshot.getUsdValue();
                    BigDecimal previousLots = lots.stream()
                            .filter(lot -> lot.getDateTime().isBefore(addressSnapshot.getDateTime()))
                            .map(AddressFiatLot::getUsdValue)
                            .reduce(ZERO, BigDecimal::add);
                    snapshots.merge(key, usdValue.subtract(previousLots), BigDecimal::add);
                });
    }
}
