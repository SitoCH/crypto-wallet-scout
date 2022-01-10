package ch.grignola.service.scanner.terra;

import ch.grignola.model.Allocation;
import ch.grignola.model.Network;
import ch.grignola.service.token.TokenPriceProvider;
import ch.grignola.service.scanner.TokenBalance;
import ch.grignola.service.scanner.terra.model.TerraBalanceResult;
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

    @Inject
    protected TokenPriceProvider tokenPriceProvider;

    @Inject
    @RestClient
    TerraRestClient terraRestClient;

    @Override
    public boolean accept(String address) {
        return address.startsWith("terra") && address.length() == 44;
    }

    @Override
    public List<TokenBalance> getAddressBalance(String address) {
        TerraBalanceResult result = terraRestClient.getBalance(address);

        List<TokenBalance> balances = new ArrayList<>();


        BigDecimal liquidValue = new BigDecimal(result.availableLuna);
        if (liquidValue.compareTo(ZERO) != 0) {
            balances.add(toTokenBalance(address, LIQUID, liquidValue));
        }

        if (result.myDelegations != null) {
            BigDecimal allocatedValue = result.myDelegations.stream().map(x -> new BigDecimal(x.amountDelegated)).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (allocatedValue.compareTo(ZERO) != 0) {
                balances.add(toTokenBalance(address, STACKED, allocatedValue));
            }
        }

        if (result.rewards != null) {
            BigDecimal unclaimedRewardsValue = new BigDecimal(result.rewards.total);
            if (unclaimedRewardsValue.compareTo(ZERO) != 0) {
                balances.add(toTokenBalance(address, UNCLAIMED_REWARDS, unclaimedRewardsValue));
            }
        }

        return balances;
    }

    private TokenBalance toTokenBalance(String address, Allocation allocation, BigDecimal value) {
        BigDecimal tokenDigits = new BigDecimal("1000000");
        BigDecimal nativeValue = value.divide(tokenDigits, MathContext.DECIMAL64);
        BigDecimal usdValue = nativeValue.equals(ZERO) ? ZERO : nativeValue.multiply(BigDecimal.valueOf(tokenPriceProvider.getUsdValue("LUNA")));
        LOG.infof("Token balance for address %s on Terra: %s (%s USD)", address, nativeValue, usdValue);
        return new TokenBalance(Network.TERRA, allocation, nativeValue, usdValue, "LUNA", "Terra");
    }
}
