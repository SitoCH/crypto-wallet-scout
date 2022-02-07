package ch.grignola.service.scanner.cronos;

import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.cronos.model.CronosBalanceResult;
import ch.grignola.service.scanner.cronos.model.Result;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class CronosScanServiceImplTest {

    private static final String ADDRESS = "cronos1234";

    @InjectMock
    @RestClient
    CronosRestClient cronosRestClient;

    @Inject
    CronosScanService cronosScanService;

    @BeforeEach
    public void setup() {
        CronosBalanceResult cronosBalanceResult = new CronosBalanceResult();
        cronosBalanceResult.result = new Result();
        cronosBalanceResult.result.balance = emptyList();
        cronosBalanceResult.result.bondedBalance = emptyList();
        cronosBalanceResult.result.totalRewards = emptyList();
        when(cronosRestClient.getBalance(eq(ADDRESS)))
                .thenReturn(cronosBalanceResult);
    }

    @Test
    void getEmptyAddressBalance() {
        List<ScannerTokenBalance> balance = cronosScanService.getAddressBalance(ADDRESS, emptyMap());

        verify(cronosRestClient).getBalance(eq(ADDRESS));

        assertTrue(balance.isEmpty());
    }
}