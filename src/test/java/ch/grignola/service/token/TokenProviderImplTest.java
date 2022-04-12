package ch.grignola.service.token;

import ch.grignola.model.Token;
import ch.grignola.repository.TokenRepository;
import ch.grignola.service.token.model.*;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class TokenProviderImplTest {

    @InjectMock
    @RestClient
    CoingeckoRestClient coingeckoRestClient;

    @InjectMock
    TokenRepository tokenRepository;

    @Inject
    TokenProvider tokenProvider;

    @BeforeEach
    public void setup() {

    }

    @Test
    void refreshCache() {

        Token token = new Token();
        token.setId(1L);
        token.setCoinGeckoId("cg-1");
        when(tokenRepository.streamAll()).thenReturn(Stream.of(token));

        when(coingeckoRestClient.getCoins()).thenReturn(emptyList());

        CoingeckoCoinMarket ccm = new CoingeckoCoinMarket();
        ccm.id = "ccm-1";
        when(coingeckoRestClient.markets(anyString(), eq(token.getCoinGeckoId()), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(singletonList(ccm));

        tokenProvider.refreshCache();

        verify(coingeckoRestClient).getCoins();
        verify(coingeckoRestClient).markets(anyString(), eq(token.getCoinGeckoId()), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void getBySymbolNotFound() {

        CoingeckoCoin coin = new CoingeckoCoin();
        coin.id = "CG-TKN1-ID";
        coin.symbol = "CG-TKN1";
        coin.name = "CG-TKN-NAME";
        when(coingeckoRestClient.getCoins()).thenReturn(singletonList(coin));

        when(tokenRepository.findBySymbol(coin.symbol)).thenReturn(empty());

        CoingeckoCoinDetail coinDetail = new CoingeckoCoinDetail();
        coinDetail.id = coin.id;
        coinDetail.name = coin.name;
        coinDetail.symbol = coin.symbol;
        coinDetail.image = new Image();
        coinDetail.image.large = "large image url";
        coinDetail.marketData = new MarketData();
        coinDetail.marketData.currentPrice = new CurrentPrice();
        when(coingeckoRestClient.get(coin.id)).thenReturn(coinDetail);

        doAnswer(invocation -> {
            ((Token) invocation.getArgument(0)).setId(1L);
            return null;
        }).when(tokenRepository).persistAndFlush(any());

        Optional<TokenDetail> tokenDetail = tokenProvider.getBySymbol(coin.symbol);

        verify(tokenRepository).persistAndFlush(any());

        assertTrue(tokenDetail.isPresent());
        assertEquals(coin.symbol, tokenDetail.get().symbol());
    }
}