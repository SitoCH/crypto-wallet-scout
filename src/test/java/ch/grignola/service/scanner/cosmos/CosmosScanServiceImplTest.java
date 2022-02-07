package ch.grignola.service.scanner.cosmos;

import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.cosmos.model.CosmosBalance;
import ch.grignola.service.scanner.cosmos.model.CosmosRewardsBalance;
import ch.grignola.service.scanner.cosmos.model.CosmosRewardsBalanceResult;
import ch.grignola.service.scanner.cosmos.model.CosmosStackedBalance;
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
class CosmosScanServiceImplTest {

    private static final String ADDRESS = "cronos1234";

    @InjectMock
    @RestClient
    CosmosRestClient cosmosRestClient;

    @Inject
    CosmosScanService cosmosScanService;

    @BeforeEach
    public void setup() {
        CosmosBalance balance = new CosmosBalance();
        balance.result = emptyList();
        when(cosmosRestClient.getBalance(ADDRESS))
                .thenReturn(balance);

        CosmosStackedBalance stackedBalance = new CosmosStackedBalance();
        stackedBalance.result = emptyList();
        when(cosmosRestClient.getStackedBalance(ADDRESS))
                .thenReturn(stackedBalance);

        CosmosRewardsBalance rewardsBalance = new CosmosRewardsBalance();
        rewardsBalance.result = new CosmosRewardsBalanceResult();
        rewardsBalance.result.total = emptyList();
        when(cosmosRestClient.getRewardsBalance(ADDRESS))
                .thenReturn(rewardsBalance);
    }

    @Test
    void getEmptyAddressBalance() {
        List<ScannerTokenBalance> balance = cosmosScanService.getAddressBalance(ADDRESS, emptyMap());

        verify(cosmosRestClient).getBalance(ADDRESS);

        assertTrue(balance.isEmpty());
    }
}