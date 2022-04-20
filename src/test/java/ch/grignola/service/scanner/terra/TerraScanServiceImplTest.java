package ch.grignola.service.scanner.terra;

import ch.grignola.model.TerraTokenContract;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class TerraScanServiceImplTest {

    private static final String ADDRESS = "terra000000000000000000000000000000000000000";

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
    void accept() {
        assertTrue(terraScanService.accept(ADDRESS));
        assertFalse(terraScanService.accept("12345"));
    }

    @Test
    void getEmptyAddressBalance() {
        List<ScannerTokenBalance> balance = terraScanService.getAddressBalance(ADDRESS, emptyMap());

        verify(terraRestClient).getBalances(ADDRESS);
        verify(terraTokenContractRepository).streamAll();

        assertTrue(balance.isEmpty());
    }

    @Test
    void getUstAddressBalance() {
        TerraBalancesResponse balance = getBalancesResponse("10000000");
        balance.balances.get(0).denom = "uusd";
        when(terraRestClient.getBalances(ADDRESS))
                .thenReturn(balance);

        List<ScannerTokenBalance> result = terraScanService.getAddressBalance(ADDRESS, emptyMap());

        verify(terraRestClient).getBalances(ADDRESS);
        verify(terraTokenContractRepository).streamAll();

        assertEquals(1, result.size());
        assertEquals(new BigDecimal(10), result.get(0).nativeValue());
        assertEquals("UST", result.get(0).tokenSymbol());
    }

    @Test
    void getAddressBalance() {

        when(terraRestClient.getBalances(ADDRESS))
                .thenReturn(getBalancesResponse("10000000"));

        List<ScannerTokenBalance> result = terraScanService.getAddressBalance(ADDRESS, emptyMap());

        verify(terraRestClient).getBalances(ADDRESS);
        verify(terraTokenContractRepository).streamAll();

        assertEquals(1, result.size());
        assertEquals(new BigDecimal(10), result.get(0).nativeValue());
        assertEquals("LUNA", result.get(0).tokenSymbol());
    }

    @Test
    void getFullAddressBalance() {

        when(terraRestClient.getBalances(ADDRESS))
                .thenReturn(getBalancesResponse("5000000"));

        when(terraRestClient.getStacking(ADDRESS))
                .thenReturn(getStackingResponse("2000000"));

        when(terraRestClient.getRewards(ADDRESS))
                .thenReturn(getRewardsResponse("100000"));

        List<ScannerTokenBalance> result = terraScanService.getAddressBalance(ADDRESS, emptyMap());

        verify(terraRestClient).getBalances(ADDRESS);
        verify(terraTokenContractRepository).streamAll();

        assertEquals(3, result.size());
        assertEquals(new BigDecimal("7.1"), result.stream().map(ScannerTokenBalance::nativeValue).reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private TerraRewardsResponse getRewardsResponse(String amount) {
        TerraRewardsResponse response = new TerraRewardsResponse();
        Rewards rewards = new Rewards();
        Reward reward = new Reward();
        reward.amount = amount;
        reward.denom = "uluna";
        rewards.reward = singletonList(reward);
        response.rewards = singletonList(rewards);
        return response;
    }

    private TerraStackingResponse getStackingResponse(String amount) {
        TerraStackingResponse response = new TerraStackingResponse();
        DelegationResponse delegationResponse = new DelegationResponse();
        Balance balance = new Balance();
        balance.amount = amount;
        balance.denom = "uluna";
        delegationResponse.balance = balance;
        response.delegationResponses = singletonList(delegationResponse);
        return response;
    }

    private TerraBalancesResponse getBalancesResponse(String amount) {
        TerraBalancesResponse response = new TerraBalancesResponse();
        Balance balance = new Balance();
        balance.amount = amount;
        balance.denom = "uluna";
        response.balances = singletonList(balance);
        return response;
    }

    private TerraTokenContract getTerraTokenContract() {
        TerraTokenContract terraTokenContract = new TerraTokenContract();
        terraTokenContract.setContractId("contract-id");
        terraTokenContract.setDecimals(5L);
        terraTokenContract.setSymbol("contract-symbol");
        return terraTokenContract;
    }

    @Test
    void getAddressBalanceWithContract() {

        TerraTokenContract terraTokenContract = getTerraTokenContract();
        when(terraTokenContractRepository.streamAll())
                .thenReturn(Stream.of(terraTokenContract));

        TerraContractBalanceResponse contractBalanceResponse = new TerraContractBalanceResponse();
        contractBalanceResponse.queryResult = new QueryResult();
        contractBalanceResponse.queryResult.balance = 10000000;
        when(terraRestClient.getContractBalance(eq(terraTokenContract.getContractId()), anyString()))
                .thenReturn(contractBalanceResponse);

        List<ScannerTokenBalance> result = terraScanService.getAddressBalance(ADDRESS, emptyMap());

        verify(terraRestClient).getBalances(ADDRESS);
        verify(terraTokenContractRepository).streamAll();

        assertEquals(1, result.size());
        assertEquals(new BigDecimal(100), result.get(0).nativeValue());
        assertEquals(terraTokenContract.getSymbol(), result.get(0).tokenSymbol());
    }

    @Test
    void getZeroBalanceContract() {

        TerraTokenContract terraTokenContract = getTerraTokenContract();
        when(terraTokenContractRepository.streamAll())
                .thenReturn(Stream.of(terraTokenContract));

        TerraContractBalanceResponse contractBalanceResponse = new TerraContractBalanceResponse();
        contractBalanceResponse.queryResult = new QueryResult();
        contractBalanceResponse.queryResult.balance = 0;
        when(terraRestClient.getContractBalance(eq(terraTokenContract.getContractId()), anyString()))
                .thenReturn(contractBalanceResponse);

        List<ScannerTokenBalance> result = terraScanService.getAddressBalance(ADDRESS, emptyMap());

        verify(terraRestClient).getBalances(ADDRESS);
        verify(terraTokenContractRepository).streamAll();

        assertEquals(0, result.size());
    }
}