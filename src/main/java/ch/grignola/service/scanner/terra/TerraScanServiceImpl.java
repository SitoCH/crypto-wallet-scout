package ch.grignola.service.scanner.terra;

import ch.grignola.model.Allocation;
import ch.grignola.model.Network;
import ch.grignola.service.scanner.TokenBalance;
import ch.grignola.service.scanner.terra.model.TerraBalanceResult;
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
public class TerraScanServiceImpl implements TerraScanService {

    private static final Logger LOG = Logger.getLogger(TerraScanServiceImpl.class);
    private static final String SYMBOL = "LUNA";

    @Inject
    protected TokenProvider tokenProvider;

    @Inject
    @RestClient
    TerraRestClient terraRestClient;

    @Override
    public boolean accept(String address) {
        return address.startsWith("terra") && address.length() == 44;
    }

    @Override
    public List<TokenBalance> getAddressBalance(String address) {
        String image = tokenProvider.getImageSmall(SYMBOL);

        TerraBalanceResult result = terraRestClient.getBalance(address);

        List<TokenBalance> balances = new ArrayList<>();

        BigDecimal liquidValue = new BigDecimal(result.availableLuna);
        if (liquidValue.compareTo(ZERO) != 0) {
            balances.add(toTokenBalance(address, image, LIQUID, liquidValue));
        }

        if (result.myDelegations != null) {
            BigDecimal allocatedValue = result.myDelegations.stream().map(x -> new BigDecimal(x.amountDelegated)).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (allocatedValue.compareTo(ZERO) != 0) {
                balances.add(toTokenBalance(address, image, STACKED, allocatedValue));
            }
        }

        if (result.rewards != null) {
            BigDecimal unclaimedRewardsValue = new BigDecimal(result.rewards.total);
            if (unclaimedRewardsValue.compareTo(ZERO) != 0) {
                balances.add(toTokenBalance(address, image, UNCLAIMED_REWARDS, unclaimedRewardsValue));
            }
        }

        return balances;
    }

    private TokenBalance toTokenBalance(String address, String image, Allocation allocation, BigDecimal value) {
        BigDecimal tokenDigits = new BigDecimal("1000000");
        BigDecimal nativeValue = value.divide(tokenDigits, MathContext.DECIMAL64);
        BigDecimal usdValue = nativeValue.equals(ZERO) ? ZERO : nativeValue.multiply(BigDecimal.valueOf(tokenProvider.getUsdValue(SYMBOL)));
        LOG.infof("Token balance for address %s on Terra: %s (%s USD)", address, nativeValue, usdValue);
        return new TokenBalance(Network.TERRA, allocation, nativeValue, usdValue, SYMBOL, "Terra", image);
    }
}
