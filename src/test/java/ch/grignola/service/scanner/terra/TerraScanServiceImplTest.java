package ch.grignola.service.scanner.terra;

import ch.grignola.repository.TerraTokenContractRepository;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.terra.model.*;
import io.quarkus.cache.CacheManager;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class TerraScanServiceImplTest {

    private static final String ADDRESS = "terra1234";

    @InjectMock
    @RestClient
    TerraRestClient terraRestClient;

    @Inject
    CacheManager cacheManager;

    @InjectMock
    TerraTokenContractRepository terraTokenContractRepository;

    @Inject
    TerraScanService terraScanService;

    @BeforeEach
    public void setup() {
        TerraBalancesResponse balancesResponse = new TerraBalancesResponse();
        when(terraRestClient.getBalances(ADDRESS))
                .thenReturn(balancesResponse);

        TerraStackingResponse stackingResponse = new TerraStackingResponse();
        when(terraRestClient.getStacking(ADDRESS))
                .thenReturn(stackingResponse);

        TerraRewardsResponse rewardsResponse = new TerraRewardsResponse();
        when(terraRestClient.getRewards(ADDRESS))
                .thenReturn(rewardsResponse);

        TerraContractBalanceResponse contractsResponse = new TerraContractBalanceResponse();
        contractsResponse.queryResult = new QueryResult();
        contractsResponse.queryResult.balance = 0;
        when(terraRestClient.getContractBalance(anyString(), anyString()))
                .thenReturn(contractsResponse);

        when(terraTokenContractRepository.streamAll())
                .thenReturn(Stream.empty());

        cacheManager.getCache("terra-cache").orElseThrow(NoSuchElementException::new).invalidateAll().await().indefinitely();
    }

    @Test
    void getEmptyAddressBalance() {
        List<ScannerTokenBalance> balance = terraScanService.getAddressBalance(ADDRESS, emptyMap());

        verify(terraRestClient).getBalances(ADDRESS);
        verify(terraTokenContractRepository).streamAll();

        assertTrue(balance.isEmpty());
    }

    @Test
    void getAddressBalance() {

        TerraBalancesResponse balancesResponse = new TerraBalancesResponse();
        Balance balance = new Balance();
        balance.amount = "10000000";
        balance.denom = "uluna";
        balancesResponse.balances = singletonList(balance);
        when(terraRestClient.getBalances(ADDRESS))
                .thenReturn(balancesResponse);

        List<ScannerTokenBalance> result = terraScanService.getAddressBalance(ADDRESS, emptyMap());

        verify(terraRestClient).getBalances(ADDRESS);
        verify(terraTokenContractRepository).streamAll();

        assertEquals(1, result.size());
        assertEquals(new BigDecimal(10), result.get(0).getNativeValue());
    }
}