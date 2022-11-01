package ch.grignola.service.scanner.dogecoin;

import ch.grignola.service.scanner.bitquery.BitqueryClient;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class DogecoinScanServiceTest {
    private static final String ADDRESS = "D1234";

    @InjectMock
    BitqueryClient bitqueryClient;

    @Inject
    DogecoinScanService dogecoinScanService;

    @BeforeEach
    public void setup() {
        when(bitqueryClient.getDogecoinBalances(ADDRESS))
                .thenReturn(50d);
    }

    @Test
    void testGetBitcoinBalances() {
        List<ScannerTokenBalance> balance = dogecoinScanService.getAddressBalance(ADDRESS);

        verify(bitqueryClient).getDogecoinBalances(ADDRESS);

        assertEquals(1, balance.size());
        assertEquals(BigDecimal.valueOf(50f), balance.get(0).nativeValue());
    }
}