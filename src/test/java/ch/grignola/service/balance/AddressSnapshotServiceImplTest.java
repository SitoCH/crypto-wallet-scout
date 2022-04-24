package ch.grignola.service.balance;

import ch.grignola.model.AddressFiatLot;
import ch.grignola.model.AddressSnapshot;
import ch.grignola.repository.AddressFiatLotRepository;
import ch.grignola.repository.AddressSnapshotRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.math.BigDecimal.ZERO;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@QuarkusTest
class AddressSnapshotServiceImplTest {

    private static final String ADDRESS = "1234";

    @InjectMock
    AddressSnapshotRepository addressSnapshotRepository;

    @InjectMock
    AddressFiatLotRepository addressFiatLotRepository;

    @Inject
    AddressSnapshotService addressSnapshotService;

    @Test
    void getHistoricalAddressBalance() {

        AddressSnapshot snapshot = getAddressSnapshot();
        when(addressSnapshotRepository.findByAddress(ADDRESS))
                .thenReturn(singletonList(snapshot));

        Map<OffsetDateTime, BigDecimal> result = addressSnapshotService.getHistoricalAddressBalance(ADDRESS);

        verify(addressSnapshotRepository).findByAddress(ADDRESS);
        verify(addressFiatLotRepository, never()).findByAddress(ADDRESS);

        assertEquals(1, result.size());
        Optional<Map.Entry<OffsetDateTime, BigDecimal>> item = result.entrySet().stream().findFirst();
        assertTrue(item.isPresent());
        assertEquals(BigDecimal.valueOf(500), item.get().getValue());
    }

    private AddressSnapshot getAddressSnapshot() {
        AddressSnapshot snapshot = new AddressSnapshot();
        snapshot.setUsdValue(BigDecimal.valueOf(500));
        snapshot.setAddress(ADDRESS);
        snapshot.setDateTime(OffsetDateTime.of(2022, 4, 24, 9, 0, 0, 0, ZoneOffset.UTC));
        return snapshot;
    }

    @Test
    void getHistoricalAddressesBalance() {

        AddressSnapshot snapshot = getAddressSnapshot();
        when(addressSnapshotRepository.findByAddress(ADDRESS))
                .thenReturn(singletonList(snapshot));

        Map<OffsetDateTime, BigDecimal> result = addressSnapshotService.getHistoricalAddressesBalance(singletonList(ADDRESS));

        verify(addressSnapshotRepository).findByAddress(ADDRESS);
        verify(addressFiatLotRepository, never()).findByAddress(ADDRESS);

        assertEquals(1, result.size());
        Optional<Map.Entry<OffsetDateTime, BigDecimal>> item = result.entrySet().stream().findFirst();
        assertTrue(item.isPresent());
        assertEquals(BigDecimal.valueOf(500), item.get().getValue());

    }

    @Test
    void getHistoricalAddressBalanceWithFiatLots() {
        AddressSnapshot snapshot = getAddressSnapshot();
        when(addressSnapshotRepository.findLastDailySnapshotByAddress(ADDRESS))
                .thenReturn(singletonList(snapshot));

        AddressFiatLot lot = new AddressFiatLot();
        lot.setUsdValue(BigDecimal.valueOf(200));
        lot.setAddress(ADDRESS);
        lot.setDateTime(OffsetDateTime.of(2022, 4, 24, 8, 0, 0, 0, ZoneOffset.UTC));
        when(addressFiatLotRepository.findByAddress(ADDRESS))
                .thenReturn(singletonList(lot));

        Map<LocalDate, BigDecimal> result = addressSnapshotService.getHistoricalAddressBalanceWithFiatLots(ADDRESS);

        verify(addressSnapshotRepository, never()).findByAddress(ADDRESS);
        verify(addressSnapshotRepository).findLastDailySnapshotByAddress(ADDRESS);
        verify(addressFiatLotRepository).findByAddress(ADDRESS);

        assertEquals(1, result.size());
        Optional<Map.Entry<LocalDate, BigDecimal>> item = result.entrySet().stream().findFirst();
        assertTrue(item.isPresent());
        assertEquals(BigDecimal.valueOf(300), item.get().getValue());
    }

    @Test
    void getHistoricalAddressBalanceWithMultipleFiatLots() {
        AddressSnapshot snapshot = getAddressSnapshot();
        when(addressSnapshotRepository.findLastDailySnapshotByAddress(ADDRESS))
                .thenReturn(singletonList(snapshot));

        AddressFiatLot lot1 = new AddressFiatLot();
        lot1.setUsdValue(BigDecimal.valueOf(200));
        lot1.setAddress(ADDRESS);
        lot1.setDateTime(OffsetDateTime.of(2022, 4, 24, 8, 0, 0, 0, ZoneOffset.UTC));
        AddressFiatLot lot2 = new AddressFiatLot();
        lot2.setUsdValue(BigDecimal.valueOf(250));
        lot2.setAddress(ADDRESS);
        lot2.setDateTime(OffsetDateTime.of(2022, 4, 20, 8, 0, 0, 0, ZoneOffset.UTC));
        when(addressFiatLotRepository.findByAddress(ADDRESS))
                .thenReturn(List.of(lot1, lot2));

        Map<LocalDate, BigDecimal> result = addressSnapshotService.getHistoricalAddressBalanceWithFiatLots(ADDRESS);

        verify(addressSnapshotRepository, never()).findByAddress(ADDRESS);
        verify(addressSnapshotRepository).findLastDailySnapshotByAddress(ADDRESS);
        verify(addressFiatLotRepository).findByAddress(ADDRESS);

        assertEquals(1, result.size());
        assertEquals(BigDecimal.valueOf(50), result.values().stream().reduce(ZERO, BigDecimal::add));
    }
}
