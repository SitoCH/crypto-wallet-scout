package ch.grignola.service.scanner.cronos;

import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.cronos.model.*;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        when(cronosRestClient.getBalance(ADDRESS))
                .thenReturn(cronosBalanceResult);
    }

    @Test
    void getEmptyAddressBalance() {
        List<ScannerTokenBalance> balance = cronosScanService.getAddressBalance(ADDRESS, emptyMap());

        verify(cronosRestClient).getBalance(ADDRESS);

        assertTrue(balance.isEmpty());
    }

    @Test
    void getFullAddressBalance() {

        when(cronosRestClient.getBalance(ADDRESS))
                .thenReturn(getBalanceResponse());

        List<ScannerTokenBalance> result = cronosScanService.getAddressBalance(ADDRESS, emptyMap());

        verify(cronosRestClient).getBalance(ADDRESS);

        assertEquals(3, result.size());
        assertEquals(new BigDecimal("7.1"), result.stream().map(ScannerTokenBalance::nativeValue).reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private CronosBalanceResult getBalanceResponse() {
        CronosBalanceResult result = new CronosBalanceResult();
        result.result = new Result();
        Balance balance = new Balance();
        balance.amount = "500000000";
        result.result.balance = singletonList(balance);
        BondedBalance bondedBalance = new BondedBalance();
        bondedBalance.amount = "200000000";
        result.result.bondedBalance = singletonList(bondedBalance);
        TotalReward reward = new TotalReward();
        reward.amount = "10000000";
        result.result.totalRewards = singletonList(reward);
        return result;
    }
}