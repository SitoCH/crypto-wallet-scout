package ch.grignola.service.scanner.terra;

import ch.grignola.model.Allocation;
import ch.grignola.model.Network;
import ch.grignola.service.quote.TokenPriceProvider;
import ch.grignola.service.scanner.TokenBalance;
import ch.grignola.service.scanner.terra.model.TerraBalanceResult;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import static ch.grignola.model.Allocation.LIQUID;
import static ch.grignola.model.Allocation.STACKED;
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


        BigDecimal liquidValue = new BigDecimal(result.balance.stream().findFirst().map(x -> x.available).orElse(BigInteger.ZERO));
        if (liquidValue.compareTo(ZERO) != 0) {
            balances.add(toTokenBalance(address, LIQUID, liquidValue));
        }

        if (result.delegations != null) {
            BigDecimal allocatedValue = result.delegations.stream().map(x -> new BigDecimal(x.amount)).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (allocatedValue.compareTo(ZERO) != 0) {
                balances.add(toTokenBalance(address, STACKED, allocatedValue));
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
