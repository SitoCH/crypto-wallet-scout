package ch.grignola.service.scanner.avalanche;

import ch.grignola.model.ContractVerificationStatus;
import ch.grignola.repository.ContractVerificationStatusRepository;
import ch.grignola.service.scanner.bitquery.BitqueryClient;
import ch.grignola.service.scanner.bitquery.model.BitqueryEthereumBalance;
import ch.grignola.service.scanner.bitquery.model.Currency;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static ch.grignola.model.ContractVerificationStatus.Status.BANNED;
import static ch.grignola.model.Network.AVALANCHE;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class AvalancheScanServiceImplTest {

    private static final String ADDRESS = "0x1234";
    private static final String TST_SYMBOL = "TST";
    private static final String TST_TKN_ADDRESS = "0x1234";

    @InjectMock
    BitqueryClient bitqueryClient;

    @InjectMock
    ContractVerificationStatusRepository contractVerificationStatusRepository;

    @Inject
    AvalancheScanService avalancheScanService;

    @BeforeEach
    public void setup() {

        when(bitqueryClient.getEthereumBalances(any(), eq(ADDRESS)))
                .thenReturn(emptyList());
    }

    @Test
    void getEmptyAddressBalance() {
        List<ScannerTokenBalance> balance = avalancheScanService.getAddressBalance(ADDRESS);

        verify(bitqueryClient).getEthereumBalances(any(), eq(ADDRESS));

        assertTrue(balance.isEmpty());
    }

    @Test
    void getSimpleAddressBalanceWithoutMatic() {

        BitqueryEthereumBalance balance = new BitqueryEthereumBalance();
        balance.currency = new Currency();
        balance.currency.symbol = TST_SYMBOL;
        balance.currency.address = TST_TKN_ADDRESS;
        balance.value = 45.5;

        when(bitqueryClient.getEthereumBalances(any(), eq(ADDRESS)))
                .thenReturn(singletonList(balance));

        List<ScannerTokenBalance> addressBalance = avalancheScanService.getAddressBalance(ADDRESS);

        verify(bitqueryClient).getEthereumBalances(any(), eq(ADDRESS));

        assertEquals(1, addressBalance.size());
        assertEquals(new BigDecimal("45.5"), addressBalance.get(0).nativeValue());
        assertEquals(TST_SYMBOL, addressBalance.get(0).tokenSymbol());
    }

    @Test
    void getSimpleAddressBalanceWithBannedContract() {

        ContractVerificationStatus contractVerificationStatus = new ContractVerificationStatus();
        contractVerificationStatus.setContractId(TST_TKN_ADDRESS);
        contractVerificationStatus.setNetwork(AVALANCHE);
        contractVerificationStatus.setStatus(BANNED);
        when(contractVerificationStatusRepository.findByNetwork(AVALANCHE))
                .thenReturn(singletonList(contractVerificationStatus));


        BitqueryEthereumBalance balance = new BitqueryEthereumBalance();
        balance.currency = new Currency();
        balance.currency.symbol = TST_SYMBOL;
        balance.currency.address = TST_TKN_ADDRESS;
        balance.value = 45.5;

        when(bitqueryClient.getEthereumBalances(any(), eq(ADDRESS)))
                .thenReturn(singletonList(balance));

        List<ScannerTokenBalance> addressBalance = avalancheScanService.getAddressBalance(ADDRESS);

        verify(bitqueryClient).getEthereumBalances(any(), eq(ADDRESS));
        verify(contractVerificationStatusRepository).findByNetwork(AVALANCHE);

        assertTrue(addressBalance.isEmpty());
    }
}