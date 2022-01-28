package ch.grignola.service.scanner.avalanche;

import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.ethereum.EthereumTokenBalanceResult;
import ch.grignola.service.scanner.ethereum.EthereumTokenEventResult;
import ch.grignola.service.scanner.ethereum.EthereumTokenEventsResult;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@QuarkusTest
class AvalancheScanServiceImplTest {

    private static final String ADDRESS = "0x1234";
    private static final String TST_CONTRACT = "0xTEST1234";
    private static final String TST_SYMBOL = "TST";
    private static final String TST_NAME = "Test token";
    private static final String TST_DECIMALS = "2";

    @InjectMock
    @RestClient
    AvalancheScanRestClient avalancheScanRestClient;

    @Inject
    AvalancheScanService avalancheScanService;


    @BeforeEach
    public void setup() {
        EthereumTokenEventsResult eventsResult = new EthereumTokenEventsResult();
        eventsResult.result = emptyList();

        when(avalancheScanRestClient.getTokenEvents(any(), any(), eq(ADDRESS)))
                .thenReturn(eventsResult);

        when(avalancheScanRestClient.getTokenBalance(any(), any(), eq(ADDRESS), any()))
                .thenReturn(new EthereumTokenBalanceResult());

        EthereumTokenBalanceResult networkTokenBalanceResult = new EthereumTokenBalanceResult();
        networkTokenBalanceResult.result = new BigInteger("0");

        when(avalancheScanRestClient.getBalance(any(), any(), eq(ADDRESS)))
                .thenReturn(networkTokenBalanceResult);
    }

    @Test
    void getEmptyAddressBalance() {
        List<ScannerTokenBalance> balance = avalancheScanService.getAddressBalance(ADDRESS);

        verify(avalancheScanRestClient).getTokenEvents(any(), any(), eq(ADDRESS));
        verify(avalancheScanRestClient, never()).getTokenBalance(any(), any(), eq(ADDRESS), any());

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

        when(avalancheScanRestClient.getTokenEvents(any(), any(), eq(ADDRESS)))
                .thenReturn(eventsResult);

        EthereumTokenBalanceResult balanceResult = new EthereumTokenBalanceResult();
        balanceResult.result = new BigInteger("4550");

        when(avalancheScanRestClient.getTokenBalance(any(), any(), eq(ADDRESS), eq(TST_CONTRACT)))
                .thenReturn(balanceResult);

        List<ScannerTokenBalance> addressBalance = avalancheScanService.getAddressBalance(ADDRESS);

        verify(avalancheScanRestClient).getTokenEvents(any(), any(), eq(ADDRESS));
        verify(avalancheScanRestClient).getTokenBalance(any(), any(), eq(ADDRESS), eq(TST_CONTRACT));

        assertEquals(1, addressBalance.size());
        assertEquals(new BigDecimal("45.5"), addressBalance.get(0).getNativeValue());
        assertEquals(TST_SYMBOL, addressBalance.get(0).getTokenSymbol());
    }
}