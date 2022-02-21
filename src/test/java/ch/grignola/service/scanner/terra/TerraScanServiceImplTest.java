package ch.grignola.service.scanner.terra;

import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.terra.model.*;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
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
    }

    @Test
    void getEmptyAddressBalance() {
        List<ScannerTokenBalance> balance = terraScanService.getAddressBalance(ADDRESS, emptyMap());

        verify(terraRestClient).getBalances(ADDRESS);

        assertTrue(balance.isEmpty());
    }
}