package ch.grignola.service.scanner.polkadot;

import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.polkadot.common.Balances;
import ch.grignola.service.scanner.polkadot.common.Data;
import ch.grignola.service.scanner.polkadot.common.PolkadotBalanceResponse;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class PolkadotScanServiceImplTest {
    private static final String ADDRESS = "1234";
    private static final String EMPTY_AMOUNT = "0x00000000000000000000000000000000";

    @InjectMock
    @RestClient
    PolkadotRestClient polkadotRestClient;

    @Inject
    PolkadotScanService polkadotScanService;

    @BeforeEach
    public void setup() {
        PolkadotBalanceResponse balanceResponse = new PolkadotBalanceResponse();
        balanceResponse.data = new Data();
        balanceResponse.data.balances = new Balances();
        balanceResponse.data.balances.availableBalance = EMPTY_AMOUNT;
        balanceResponse.data.balances.reservedBalance = EMPTY_AMOUNT;
        when(polkadotRestClient.getBalance(ADDRESS, "Polkadot"))
                .thenReturn(balanceResponse);
    }

    @Test
    void testEmptyAddressBalance() {
        List<ScannerTokenBalance> balance = polkadotScanService.getAddressBalance(ADDRESS);

        verify(polkadotRestClient).getBalance(ADDRESS, "Polkadot");

        assertTrue(balance.isEmpty());
    }

    @Test
    void testAddressBalance() {
        PolkadotBalanceResponse balanceResponse = new PolkadotBalanceResponse();
        balanceResponse.data = new Data();
        balanceResponse.data.balances = new Balances();
        balanceResponse.data.balances.availableBalance = "0x0000000000000000000000355176b200";
        balanceResponse.data.balances.reservedBalance = EMPTY_AMOUNT;
        when(polkadotRestClient.getBalance(ADDRESS, "Polkadot"))
                .thenReturn(balanceResponse);

        List<ScannerTokenBalance> balance = polkadotScanService.getAddressBalance(ADDRESS);

        verify(polkadotRestClient).getBalance(ADDRESS, "Polkadot");

        assertEquals(1, balance.size());
        assertEquals(BigDecimal.valueOf(22.9d), balance.get(0).nativeValue());
    }
}