package ch.grignola.service.scanner.aave;

import ch.grignola.model.Network;
import ch.grignola.service.scanner.aave.model.AaveResponse;
import ch.grignola.service.scanner.common.ScannerTokenBalance;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.graphql.client.core.Document;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.JsonObject;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static ch.grignola.model.Allocation.STACKED;
import static ch.grignola.model.Allocation.UNCLAIMED_REWARDS;
import static ch.grignola.model.Network.*;
import static io.smallrye.graphql.client.core.Argument.arg;
import static io.smallrye.graphql.client.core.Argument.args;
import static io.smallrye.graphql.client.core.Document.document;
import static io.smallrye.graphql.client.core.Field.field;
import static io.smallrye.graphql.client.core.InputObject.inputObject;
import static io.smallrye.graphql.client.core.InputObjectField.prop;
import static io.smallrye.graphql.client.core.Operation.operation;
import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang3.StringUtils.rightPad;

@Singleton
public class AaveScanServiceImpl implements AaveScanService {

    private static final Logger LOG = Logger.getLogger(AaveScanServiceImpl.class);

    private static final String BALANCE_LOG = "Token balance for address %s on %s for symbol %s: %s";

    @Inject
    @GraphQLClient("aave-avalanche")
    DynamicGraphQLClient aaveAvalancheClient;

    @Inject
    @GraphQLClient("aave-polygon")
    DynamicGraphQLClient aavePolygonClient;

    @Inject
    @GraphQLClient("aave-optimism")
    DynamicGraphQLClient aaveOptimismClient;

    @Override
    public boolean accept(String address) {
        return address.startsWith("0x") && address.length() == 42;
    }

    @Override
    public List<ScannerTokenBalance> getAddressBalance(String address) {
        List<ScannerTokenBalance> balances = new ArrayList<>();

        addAaveBalances(address, aaveAvalancheClient, AVALANCHE, balances);
        addAaveBalances(address, aavePolygonClient, POLYGON, balances);
        addAaveBalances(address, aaveOptimismClient, OPTIMISM, balances);

        return balances;
    }

    private void addAaveBalances(String address, DynamicGraphQLClient client, Network network, List<ScannerTokenBalance> balances) {
        try {
            AaveResponse avalancheResult = getAaveResponse(client, address);
            avalancheResult.userRewards.stream().findFirst()
                    .ifPresent(userReward -> {
                        BigDecimal nativeBalance = getNativeBalance(userReward.user.unclaimedRewards, userReward.reward.rewardTokenDecimals);
                        LOG.infof(BALANCE_LOG, address, network, userReward.reward.rewardTokenSymbol, nativeBalance);
                        balances.add(new ScannerTokenBalance(network, UNCLAIMED_REWARDS, nativeBalance, userReward.reward.rewardTokenSymbol));
                    });

            balances.addAll(avalancheResult.userReserves.stream()
                    .map(userReserve -> {
                        List<ScannerTokenBalance> userReserveBalance = new ArrayList<>();
                        if (!userReserve.currentTotalDebt.equals(ZERO)) {
                            BigDecimal nativeBalance = getNativeBalance(userReserve.currentTotalDebt, userReserve.reserve.decimals);
                            LOG.infof(BALANCE_LOG, address, network, userReserve.reserve.symbol, nativeBalance);
                            userReserveBalance.add(new ScannerTokenBalance(network, STACKED, nativeBalance.negate(), userReserve.reserve.symbol));
                        }

                        if (!userReserve.currentATokenBalance.equals(ZERO)) {
                            BigDecimal nativeBalance = getNativeBalance(userReserve.currentATokenBalance, userReserve.reserve.decimals);
                            LOG.infof(BALANCE_LOG, address, network, userReserve.reserve.symbol, nativeBalance);
                            userReserveBalance.add(new ScannerTokenBalance(network, STACKED, nativeBalance, userReserve.reserve.symbol));
                        }
                        return userReserveBalance;
                    }).flatMap(Collection::stream).toList());

        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            Thread.currentThread().interrupt();
            LOG.warnf("Unable to load AAVE %s balance for address %s", network, address);
        }
    }

    private static BigDecimal getNativeBalance(BigDecimal tokenAmount, int tokenDecimals) {
        return tokenAmount.divide(new BigDecimal(rightPad("1", tokenDecimals + 1, '0')), MathContext.DECIMAL64);
    }

    private AaveResponse getAaveResponse(DynamicGraphQLClient client, String address) throws ExecutionException, InterruptedException, JsonProcessingException {
        Document document = document(
                operation(
                        field("userReserves",
                                args(arg("where", inputObject(prop("user", address.toLowerCase())))),
                                field("reserve",
                                        field("symbol"),
                                        field("decimals")
                                ),
                                field("currentATokenBalance"),
                                field("currentTotalDebt")
                        ),
                        field("userRewards",
                                args(arg("where", inputObject(prop("user", address.toLowerCase())))),
                                field("reward",
                                        field("rewardTokenSymbol"),
                                        field("rewardTokenDecimals")
                                ),
                                field("user",
                                        field("unclaimedRewards")
                                )
                        )
                )
        );

        JsonObject data = client.executeSync(document).getData();
        return new ObjectMapper().readValue(data.toString(), AaveResponse.class);
    }
}
