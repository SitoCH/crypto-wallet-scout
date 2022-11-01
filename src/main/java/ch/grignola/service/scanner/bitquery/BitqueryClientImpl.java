package ch.grignola.service.scanner.bitquery;

import ch.grignola.service.scanner.bitquery.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.graphql.client.core.Document;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static io.smallrye.graphql.client.core.Argument.arg;
import static io.smallrye.graphql.client.core.Argument.args;
import static io.smallrye.graphql.client.core.Document.document;
import static io.smallrye.graphql.client.core.Enum.gqlEnum;
import static io.smallrye.graphql.client.core.Field.field;
import static io.smallrye.graphql.client.core.InputObject.inputObject;
import static io.smallrye.graphql.client.core.InputObjectField.prop;
import static io.smallrye.graphql.client.core.Operation.operation;
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;

@ApplicationScoped
public class BitqueryClientImpl implements BitqueryClient {

    private static final Logger LOG = Logger.getLogger(BitqueryClientImpl.class);
    private static final String ADDRESS = "address";
    private static final String VALUE = "value";

    private final RateLimiter rateLimiter;
    @Inject
    @GraphQLClient("bitquery")
    DynamicGraphQLClient dynamicClient;
    @Inject
    @CacheName("bitquery-cache")
    Cache cache;

    public BitqueryClientImpl() {
        rateLimiter = RateLimiter.of("BitqueryClient", RateLimiterConfig.custom()
                .timeoutDuration(ofSeconds(30))
                .limitRefreshPeriod(ofMinutes(1))
                .limitForPeriod(10)
                .build());
    }

    @Override
    public double getDogecoinBalances(String address) {
        String key = "dogecoin-" + address;
        return cache.get(key, x -> {
            try {
                return bitcoinBalances(address, "dogecoin");
            } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
                Thread.currentThread().interrupt();
                LOG.warnf("Unable to load Bitcoin balance for address %s", address);
                return 0d;
            }
        }).await().indefinitely();
    }

    @Override
    public double getBitcoinBalances(String address) {
        String key = "bitcoin-" + address;
        return cache.get(key, x -> {
            try {
                return bitcoinBalances(address, "bitcoin");
            } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
                Thread.currentThread().interrupt();
                LOG.warnf("Unable to load Bitcoin balance for address %s", address);
                return 0d;
            }
        }).await().indefinitely();
    }

    private double bitcoinBalances(String address, String network) throws ExecutionException, InterruptedException, JsonProcessingException {
        Document bitcoinDocument = document(
                operation(
                        field("bitcoin",
                                args(arg("network", gqlEnum(network))),
                                field("inputs",
                                        args(arg("inputAddress", inputObject(prop("is", address)))),
                                        field(VALUE)
                                ),
                                field("outputs",
                                        args(arg("outputAddress", inputObject(prop("is", address)))),
                                        field(VALUE)
                                )
                        )
                )
        );

        JsonObject data = dynamicClient.executeSync(bitcoinDocument).getData();
        rateLimiter.acquirePermission();
        BitqueryBitcoinBalance bitcoinResponse = new ObjectMapper().readValue(data.toString(), BitqueryBitcoinBalance.class);
        return getTotalBitcoins(bitcoinResponse.bitcoin);
    }

    private double getTotalBitcoins(Bitcoin bitcoin) {
        return aggregate(bitcoin.outputs) - aggregate(bitcoin.inputs);
    }

    private Double aggregate(List<BitcoinField> fields) {
        return fields.stream().map(x -> x.value).reduce(0d, Double::sum);
    }

    @Override
    @SuppressWarnings("java:S2293")
    public List<BitqueryEthereumBalance> getEthereumBalances(String network, String address) {
        String key = network + "-" + address;
        return cache.get(key, x -> {
            try {
                return ethereumBalances(network, address);
            } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
                Thread.currentThread().interrupt();
                LOG.warnf("Unable to load balance for address %s on %s", network, address);
                return new ArrayList<BitqueryEthereumBalance>();
            }
        }).await().indefinitely();
    }

    private List<BitqueryEthereumBalance> ethereumBalances(String network, String address) throws ExecutionException, InterruptedException, JsonProcessingException {
        Document ethereumDocument = document(
                operation(
                        field("ethereum",
                                args(arg("network", gqlEnum(network))),
                                field(ADDRESS,
                                        args(arg(ADDRESS, inputObject(prop("is", address)))),
                                        field("balances",
                                                field("currency",
                                                        field(ADDRESS),
                                                        field("symbol")
                                                ),
                                                field(VALUE)
                                        )
                                )
                        )
                )
        );

        JsonObject data = dynamicClient.executeSync(ethereumDocument).getData();
        rateLimiter.acquirePermission();
        BitqueryEthereumResponse ethereumBalance = new ObjectMapper().readValue(data.toString(), BitqueryEthereumResponse.class);
        return ethereumBalance.ethereum.address.stream()
                .filter(x -> x.balances != null)
                .flatMap(x -> x.balances.stream()).toList();
    }
}
