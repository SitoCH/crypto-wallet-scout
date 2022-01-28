package ch.grignola.service.scanner.terra;

import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.terra.model.TerraBalanceResult;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class TerraScanServiceImplTest {

    private static final String ADDRESS = "cronos1234";

    @InjectMock
    @RestClient
    TerraRestClient terraRestClient;

    @Inject
    TerraScanService terraScanService;

    @BeforeEach
    public void setup() {
        TerraBalanceResult balance = new TerraBalanceResult();
        balance.availableLuna = "0";
        when(terraRestClient.getBalance(ADDRESS))
                .thenReturn(balance);

    }

    @Test
    void getEmptyAddressBalance() {
        List<ScannerTokenBalance> balance = terraScanService.getAddressBalance(ADDRESS);

        verify(terraRestClient).getBalance(ADDRESS);

        assertTrue(balance.isEmpty());
    }
}