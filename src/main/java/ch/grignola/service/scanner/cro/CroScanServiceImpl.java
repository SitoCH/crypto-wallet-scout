package ch.grignola.service.scanner.cro;

import ch.grignola.model.Allocation;
import ch.grignola.model.Network;
import ch.grignola.service.scanner.TokenBalance;
import ch.grignola.service.scanner.cro.model.CroBalanceResult;
import ch.grignola.service.token.TokenProvider;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import static ch.grignola.model.Allocation.*;
import static java.math.BigDecimal.ZERO;

@ApplicationScoped
public class CroScanServiceImpl implements CroScanService {

    private static final Logger LOG = Logger.getLogger(CroScanServiceImpl.class);
    private static final String SYMBOL = "CRO";

    @Inject
    protected TokenProvider tokenProvider;

    @Inject
    @RestClient
    CroRestClient croRestClient;

    @Override
    public boolean accept(String address) {
        return address.startsWith("cro") && address.length() == 42;
    }

    @Override
    public List<TokenBalance> getAddressBalance(String address) {
        String image = tokenProvider.getImageSmall(SYMBOL);

        List<TokenBalance> balances = new ArrayList<>();
        CroBalanceResult result = croRestClient.getBalance(address);

        BigDecimal liquidValue = result.result.balance.stream().map(x -> new BigDecimal(x.amount)).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (liquidValue.compareTo(ZERO) != 0) {
            balances.add(toTokenBalance(address, image, LIQUID, liquidValue));
        }

        BigDecimal stackedValue = result.result.bondedBalance.stream().map(x -> new BigDecimal(x.amount)).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (stackedValue.compareTo(ZERO) != 0) {
            balances.add(toTokenBalance(address, image, STACKED, stackedValue));
        }

        BigDecimal unclaimedRewardsValue = result.result.totalRewards.stream().map(x -> new BigDecimal(x.amount)).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (unclaimedRewardsValue.compareTo(ZERO) != 0) {
            balances.add(toTokenBalance(address, image, UNCLAIMED_REWARDS, unclaimedRewardsValue));
        }

        return balances;
    }

    private TokenBalance toTokenBalance(String address, String image, Allocation allocation, BigDecimal value) {
        BigDecimal tokenDigits = new BigDecimal("100000000");
        BigDecimal nativeValue = value.divide(tokenDigits, MathContext.DECIMAL64);
        BigDecimal usdValue = nativeValue.equals(ZERO) ? ZERO : nativeValue.multiply(BigDecimal.valueOf(tokenProvider.getUsdValue(SYMBOL)));
        LOG.infof("Token balance for address %s on CRO: %s (%s USD)", address, nativeValue, usdValue);
        return new TokenBalance(Network.CRO, allocation, nativeValue, usdValue, SYMBOL, "Crypto.com Coin", image);
    }
}
