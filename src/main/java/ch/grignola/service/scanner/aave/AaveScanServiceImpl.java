package ch.grignola.service.scanner.aave;

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
import java.util.List;
import java.util.concurrent.ExecutionException;

import static ch.grignola.model.Allocation.STACKED;
import static ch.grignola.model.Network.AVALANCHE;
import static io.smallrye.graphql.client.core.Argument.arg;
import static io.smallrye.graphql.client.core.Argument.args;
import static io.smallrye.graphql.client.core.Document.document;
import static io.smallrye.graphql.client.core.Field.field;
import static io.smallrye.graphql.client.core.InputObject.inputObject;
import static io.smallrye.graphql.client.core.InputObjectField.prop;
import static io.smallrye.graphql.client.core.Operation.operation;
import static java.math.BigDecimal.valueOf;
import static org.apache.commons.lang3.StringUtils.rightPad;

@Singleton
public class AaveScanServiceImpl implements AaveScanService {

    private static final Logger LOG = Logger.getLogger(AaveScanServiceImpl.class);

    @Inject
    @GraphQLClient("aave-avalanche")
    DynamicGraphQLClient aaveAvalancheClient;

    @Override
    public boolean accept(String address) {
        return address.startsWith("0x") && address.length() == 42;
    }

    @Override
    public List<ScannerTokenBalance> getAddressBalance(String address) {
        List<ScannerTokenBalance> balances = new ArrayList<>();
        try {
            AaveResponse avalancheResult = getAaveResponse(address);
            avalancheResult.userRewards.stream().findFirst()
                    .ifPresent(userReward -> {
                        BigDecimal nativeBalance = valueOf(userReward.user.unclaimedRewards).divide(new BigDecimal(rightPad("1", userReward.reward.rewardTokenDecimals + 1, '0')), MathContext.DECIMAL64);
                        balances.add(new ScannerTokenBalance(AVALANCHE, STACKED, nativeBalance, userReward.reward.rewardTokenSymbol));
                    });

            balances.addAll(avalancheResult.userReserves.stream()
                    .filter(x -> x.currentTotalDebt > 0)
                    .map(userReserve -> {
                        BigDecimal nativeBalance = valueOf(userReserve.currentTotalDebt).divide(new BigDecimal(rightPad("1", userReserve.reserve.decimals + 1, '0')), MathContext.DECIMAL64);
                        return new ScannerTokenBalance(AVALANCHE, STACKED, nativeBalance.negate(), userReserve.reserve.symbol);
                    }).toList());

        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            Thread.currentThread().interrupt();
            LOG.warnf("Unable to load AAVE Avalanche balance for address %s", address);
        }

        return balances;
    }

    private AaveResponse getAaveResponse(String address) throws ExecutionException, InterruptedException, JsonProcessingException {
        Document document = document(
                operation(
                        field("userReserves",
                                args(arg("where", inputObject(prop("user", address)))),
                                field("reserve",
                                        field("symbol"),
                                        field("decimals")
                                ),
                                field("currentTotalDebt")
                        ),
                        field("userRewards",
                                args(arg("where", inputObject(prop("user", address)))),
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

        JsonObject data = aaveAvalancheClient.executeSync(document).getData();
        return new ObjectMapper().readValue(data.toString(), AaveResponse.class);
    }
}
