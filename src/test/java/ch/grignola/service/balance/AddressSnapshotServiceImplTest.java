package ch.grignola.service.balance;

import ch.grignola.model.AddressSnapshot;
import ch.grignola.repository.AddressSnapshotRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class AddressSnapshotServiceImplTest {

    private static final String ADDRESS = "1234";

    @InjectMock
    AddressSnapshotRepository addressSnapshotRepository;

    @Inject
    AddressSnapshotService addressSnapshotService;

    @Test
    void getHistoricalAddressBalance() {

        AddressSnapshot snapshot = getAddressSnapshot();
        when(addressSnapshotRepository.findByAddress(ADDRESS))
                .thenReturn(singletonList(snapshot));

        Map<OffsetDateTime, BigDecimal> result = addressSnapshotService.getHistoricalAddressBalance(ADDRESS);

        verify(addressSnapshotRepository).findByAddress(ADDRESS);

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

        assertEquals(1, result.size());
        Optional<Map.Entry<OffsetDateTime, BigDecimal>> item = result.entrySet().stream().findFirst();
        assertTrue(item.isPresent());
        assertEquals(BigDecimal.valueOf(500), item.get().getValue());

    }

}
