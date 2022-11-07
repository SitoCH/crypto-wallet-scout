package ch.grignola.service.scanner.bnb;

import ch.grignola.model.ContractVerificationStatus;
import ch.grignola.repository.AddressTokenValueRepository;
import ch.grignola.repository.ContractVerificationStatusRepository;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import ch.grignola.service.scanner.etherscan.model.EthereumTokenBalanceResult;
import ch.grignola.service.scanner.etherscan.model.EthereumTokenEventResult;
import ch.grignola.service.scanner.etherscan.model.EthereumTokenEventsResult;
import io.quarkus.cache.CacheManager;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static ch.grignola.model.ContractVerificationStatus.Status.BANNED;
import static ch.grignola.model.Network.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class BnbEtherscanServiceImplTest {

    private static final String ADDRESS = "0x1234";
    private static final String NATIVE_SYMBOL = "BNB";
    private static final String TST_SYMBOL = "TST";
    private static final String TST_TKN_ADDRESS = "0x1234";

    @InjectMock
    @RestClient
    BnbScanRestClient restClient;

    @InjectMock
    AddressTokenValueRepository addressTokenValueRepository;

    @InjectMock
    ContractVerificationStatusRepository contractVerificationStatusRepository;

    @Inject
    BnbEtherscanService bnbEtherscanService;

    @Inject
    CacheManager cacheManager;

    @BeforeEach
    public void setup() {
        EthereumTokenBalanceResult balanceResult = new EthereumTokenBalanceResult();
        balanceResult.result = BigInteger.valueOf(0);
        when(restClient.getBalance(any(), any(), eq(ADDRESS)))
                .thenReturn(balanceResult);

        EthereumTokenEventsResult tokenEventsResult = new EthereumTokenEventsResult();
        tokenEventsResult.result = emptyList();
        when(restClient.getTokenEvents(any(), any(), eq(ADDRESS)))
                .thenReturn(tokenEventsResult);

        when(addressTokenValueRepository.getLastUpdated(any(), eq(ADDRESS)))
                .thenReturn(Optional.empty());

        when(contractVerificationStatusRepository.findByNetwork(ETHEREUM))
                .thenReturn(emptyList());

        cacheManager.getCache("etherscan-cache").orElseThrow(NoSuchElementException::new).invalidateAll().await().indefinitely();
    }

    @Test
    void getEmptyAddressBalance() {
        List<ScannerTokenBalance> balance = bnbEtherscanService.getAddressBalance(ADDRESS);

        verify(restClient).getBalance(any(), any(), eq(ADDRESS));

        assertTrue(balance.isEmpty());
    }

    @Test
    void getSimpleAddressBalance() {

        EthereumTokenBalanceResult balanceResult = new EthereumTokenBalanceResult();
        balanceResult.result = new BigInteger("1000000000000000000");
        when(restClient.getBalance(any(), any(), eq(ADDRESS)))
                .thenReturn(balanceResult);

        List<ScannerTokenBalance> addressBalance = bnbEtherscanService.getAddressBalance(ADDRESS);

        verify(restClient).getBalance(any(), any(), eq(ADDRESS));

        assertEquals(1, addressBalance.size());
        assertEquals(new BigDecimal("1"), addressBalance.get(0).nativeValue());
        assertEquals(NATIVE_SYMBOL, addressBalance.get(0).tokenSymbol());
    }


    @Test
    void getAddressBalanceFromEvents() {

        EthereumTokenEventsResult tokenEvents = new EthereumTokenEventsResult();
        EthereumTokenEventResult tokenEvent = new EthereumTokenEventResult();
        tokenEvent.tokenSymbol = TST_SYMBOL;
        tokenEvent.contractAddress = TST_TKN_ADDRESS;
        tokenEvent.tokenDecimal = 1;
        tokenEvents.result = singletonList(tokenEvent);
        when(restClient.getTokenEvents(any(), any(), eq(ADDRESS)))
                .thenReturn(tokenEvents);

        EthereumTokenBalanceResult tokenBalance = new EthereumTokenBalanceResult();
        tokenBalance.result = BigInteger.TEN;
        when(restClient.getTokenBalance(any(), any(), eq(ADDRESS), eq(TST_TKN_ADDRESS)))
                .thenReturn(tokenBalance);

        List<ScannerTokenBalance> addressBalance = bnbEtherscanService.getAddressBalance(ADDRESS);

        verify(restClient).getBalance(any(), any(), eq(ADDRESS));
        verify(restClient).getTokenEvents(any(), any(), eq(ADDRESS));

        assertEquals(1, addressBalance.size());
        assertEquals(new BigDecimal("1"), addressBalance.get(0).nativeValue());
        assertEquals(TST_SYMBOL, addressBalance.get(0).tokenSymbol());
    }

    @Test
    void getSimpleAddressBalanceWithBannedContract() {

        ContractVerificationStatus contractVerificationStatus = new ContractVerificationStatus();
        contractVerificationStatus.setContractId(TST_TKN_ADDRESS);
        contractVerificationStatus.setNetwork(BNB);
        contractVerificationStatus.setStatus(BANNED);
        when(contractVerificationStatusRepository.findByNetwork(BNB))
                .thenReturn(singletonList(contractVerificationStatus));


        EthereumTokenEventsResult tokenEvents = new EthereumTokenEventsResult();
        EthereumTokenEventResult tokenEvent = new EthereumTokenEventResult();
        tokenEvent.tokenSymbol = TST_SYMBOL;
        tokenEvent.contractAddress = TST_TKN_ADDRESS;
        tokenEvent.tokenDecimal = 1;
        tokenEvents.result = singletonList(tokenEvent);
        when(restClient.getTokenEvents(any(), any(), eq(ADDRESS)))
                .thenReturn(tokenEvents);

        EthereumTokenBalanceResult tokenBalance = new EthereumTokenBalanceResult();
        tokenBalance.result = BigInteger.TEN;
        when(restClient.getTokenBalance(any(), any(), eq(ADDRESS), eq(TST_TKN_ADDRESS)))
                .thenReturn(tokenBalance);

        List<ScannerTokenBalance> addressBalance = bnbEtherscanService.getAddressBalance(ADDRESS);

        verify(restClient).getBalance(any(), any(), eq(ADDRESS));
        verify(restClient).getTokenEvents(any(), any(), eq(ADDRESS));
        verify(contractVerificationStatusRepository).findByNetwork(BNB);

        assertTrue(addressBalance.isEmpty());
    }
}