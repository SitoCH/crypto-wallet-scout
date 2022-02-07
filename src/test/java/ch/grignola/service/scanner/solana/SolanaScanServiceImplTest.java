package ch.grignola.service.scanner.solana;

import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.solana.model.SolanaNativeBalance;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class SolanaScanServiceImplTest {

    private static final String ADDRESS = "cronos1234";

    @InjectMock
    @RestClient
    SolanaRestClient solanaRestClient;

    @Inject
    SolanaScanService solanaScanService;

    @BeforeEach
    public void setup() {
        SolanaNativeBalance balance = new SolanaNativeBalance();
        balance.lamports = 0;
        when(solanaRestClient.getNativeBalance(ADDRESS))
                .thenReturn(balance);

        when(solanaRestClient.getAccountTokens(ADDRESS))
                .thenReturn(emptyList());

    }

    @Test
    void getEmptyAddressBalance() {
        List<ScannerTokenBalance> balance = solanaScanService.getAddressBalance(ADDRESS, emptyMap());

        verify(solanaRestClient).getNativeBalance(ADDRESS);

        verify(solanaRestClient).getAccountTokens(ADDRESS);

        assertTrue(balance.isEmpty());
    }
}