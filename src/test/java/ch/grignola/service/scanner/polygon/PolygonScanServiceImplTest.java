package ch.grignola.service.scanner.polygon;

import ch.grignola.service.quote.TokenPriceProvider;
import ch.grignola.service.scanner.common.EthereumTokenBalanceResult;
import ch.grignola.service.scanner.common.EthereumTokenEventResult;
import ch.grignola.service.scanner.common.EthereumTokenEventsResult;
import ch.grignola.service.scanner.model.TokenBalance;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

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

    @InjectMock
    TokenPriceProvider tokenPriceProvider;

    @Inject
    PolygonScanService polygonScanService;


    @BeforeEach
    public void setup() {
        EthereumTokenEventsResult eventsResult = new EthereumTokenEventsResult();
        eventsResult.result = emptyList();

        when(polygonScanRestClient.getTokenEvents(any(), any(), eq(ADDRESS)))
                .thenReturn(eventsResult);

        when(polygonScanRestClient.getTokenBalance(any(), any(), eq(ADDRESS), any()))
                .thenReturn(new EthereumTokenBalanceResult());
    }

    @Test
    void getEmptyAddressBalance() {
        List<TokenBalance> balance = polygonScanService.getAddressBalance(ADDRESS);

        verify(polygonScanRestClient).getTokenEvents(any(), any(), eq(ADDRESS));
        verify(polygonScanRestClient, never()).getTokenBalance(any(), any(), eq(ADDRESS), any());

        assertTrue(balance.isEmpty());
    }

    @Test
    void getSimpleAddressBalanceWithoutMatic() {

        EthereumTokenEventResult result = new EthereumTokenEventResult();
        result.contractAddress = TST_CONTRACT;
        result.tokenDecimal = TST_DECIMALS;
        result.tokenSymbol = TST_SYMBOL;
        result.tokenName = TST_NAME;

        EthereumTokenEventsResult eventsResult = new EthereumTokenEventsResult();
        eventsResult.result = singletonList(result);

        when(polygonScanRestClient.getTokenEvents(any(), any(), eq(ADDRESS)))
                .thenReturn(eventsResult);

        EthereumTokenBalanceResult balanceResult = new EthereumTokenBalanceResult();
        balanceResult.result = 4550;

        when(polygonScanRestClient.getTokenBalance(any(), any(), eq(ADDRESS), eq(TST_CONTRACT)))
                .thenReturn(balanceResult);

        when(tokenPriceProvider.getUsdValue(TST_SYMBOL)).thenReturn(0.1);

        List<TokenBalance> addressBalance = polygonScanService.getAddressBalance(ADDRESS);

        verify(polygonScanRestClient).getTokenEvents(any(), any(), eq(ADDRESS));
        verify(polygonScanRestClient).getTokenBalance(any(), any(), eq(ADDRESS), eq(TST_CONTRACT));

        assertEquals(1, addressBalance.size());
        assertEquals(new BigDecimal("45.5"), addressBalance.get(0).getNativeValue());
        assertEquals(new BigDecimal("4.55"), addressBalance.get(0).getUsdValue());
        assertEquals(TST_SYMBOL, addressBalance.get(0).getSymbol());
    }
}