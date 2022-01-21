package ch.grignola.service.scanner.polygon;

import ch.grignola.model.Allocation;
import ch.grignola.service.scanner.bitquery.BitqueryClient;
import ch.grignola.service.scanner.bitquery.model.Balance;
import ch.grignola.service.scanner.bitquery.model.Currency;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.token.TokenProvider;
import ch.grignola.service.token.model.TokenDetail;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class PolygonScanServiceImplTest {

    private static final String ADDRESS = "0x1234";
    private static final String TST_CONTRACT = "0xTEST1234";
    private static final String TST_SYMBOL = "TST";
    private static final String TST_NAME = "Test token";
    private static final String TST_DECIMALS = "2";

    @InjectMock
    BitqueryClient bitqueryClient;

    @InjectMock
    TokenProvider tokenProvider;

    @Inject
    PolygonScanService polygonScanService;


    @BeforeEach
    public void setup() {

        when(bitqueryClient.getRawBalance(any(), eq(ADDRESS)))
                .thenReturn(emptyList());

    }

    @Test
    void getEmptyAddressBalance() {
        List<ScannerTokenBalance> balance = polygonScanService.getAddressBalance(ADDRESS);

        verify(bitqueryClient).getRawBalance(any(), eq(ADDRESS));

        assertTrue(balance.isEmpty());
    }

    @Test
    void getSimpleAddressBalanceWithoutMatic() {

        Balance balance = new Balance();
        balance.currency = new Currency();
        balance.currency.symbol = TST_SYMBOL;
        balance.value = 45.5;

        when(bitqueryClient.getRawBalance(any(), eq(ADDRESS)))
                .thenReturn(singletonList(balance));

        when(tokenProvider.getBySymbol(TST_SYMBOL)).thenReturn(Optional.of(new TokenDetail(null, null, null, null, 0.1f, Allocation.LIQUID, 0f, 0f)));

        List<ScannerTokenBalance> addressBalance = polygonScanService.getAddressBalance(ADDRESS);

        verify(bitqueryClient).getRawBalance(any(), eq(ADDRESS));

        assertEquals(1, addressBalance.size());
        assertEquals(new BigDecimal("45.5"), addressBalance.get(0).getNativeValue());
        assertEquals(TST_SYMBOL, addressBalance.get(0).getTokenSymbol());
    }
}