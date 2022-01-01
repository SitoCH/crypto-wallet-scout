package ch.grignola.service.scanner.polygon;

import ch.grignola.service.scanner.AddressBalance;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@QuarkusTest
class PolygonScanServiceImplTest {

    private static final String ADDRESS = "0x1234";
    private static final String TST_CONTRACT = "0xTEST1234";
    private static final String TST_SYMBOL = "TST";
    private static final String TST_NAME = "Test token";
    private static final String TST_DECIMALS = "2";

    @InjectMock
    @RestClient
    PolygonScanRestClient polygonScanRestClient;

    @Inject
    PolygonScanService polygonScanService;


    @BeforeEach
    public void setup() {
        PolygonScanRestClient.PolygonTokenEventsResult eventsResult = new PolygonScanRestClient.PolygonTokenEventsResult();
        eventsResult.setResult(emptyList());

        when(polygonScanRestClient.getPolygonTokenEvents(any(), any(), eq(ADDRESS)))
                .thenReturn(eventsResult);

        when(polygonScanRestClient.getPolygonTokenBalance(any(), any(), eq(ADDRESS), any()))
                .thenReturn(new PolygonScanRestClient.PolygonTokenBalanceResult());
    }

    @Test
    void getEmptyAddressBalance() {
        AddressBalance balance = polygonScanService.getAddressBalance(ADDRESS);

        verify(polygonScanRestClient).getPolygonTokenEvents(any(), any(), eq(ADDRESS));
        verify(polygonScanRestClient, never()).getPolygonTokenBalance(any(), any(), eq(ADDRESS), any());

        assertTrue(balance.getTokenBalances().isEmpty());
    }

    @Test
    void getSimpleAddressBalanceWithoutMatic() {

        PolygonScanRestClient.Result result = new PolygonScanRestClient.Result();
        result.setContractAddress(TST_CONTRACT);
        result.setTokenDecimal(TST_DECIMALS);
        result.setTokenSymbol(TST_SYMBOL);
        result.setTokenName(TST_NAME);

        PolygonScanRestClient.PolygonTokenEventsResult eventsResult = new PolygonScanRestClient.PolygonTokenEventsResult();
        eventsResult.setResult(singletonList(result));

        when(polygonScanRestClient.getPolygonTokenEvents(any(), any(), eq(ADDRESS)))
                .thenReturn(eventsResult);

        PolygonScanRestClient.PolygonTokenBalanceResult balanceResult = new PolygonScanRestClient.PolygonTokenBalanceResult();
        balanceResult.setResult("4550");

        when(polygonScanRestClient.getPolygonTokenBalance(any(), any(), eq(ADDRESS), eq(TST_CONTRACT)))
                .thenReturn(balanceResult);

        AddressBalance addressBalance = polygonScanService.getAddressBalance(ADDRESS);

        verify(polygonScanRestClient).getPolygonTokenEvents(any(), any(), eq(ADDRESS));
        verify(polygonScanRestClient).getPolygonTokenBalance(any(), any(), eq(ADDRESS), eq(TST_CONTRACT));

        assertEquals(1, addressBalance.getTokenBalances().size());
        assertEquals(new BigDecimal("45.5"), addressBalance.getTokenBalances().get(0).getBalance());
        assertEquals(TST_SYMBOL, addressBalance.getTokenBalances().get(0).getSymbol());
    }
}