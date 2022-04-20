package ch.grignola.service.scanner.bitquery;

import ch.grignola.service.scanner.bitquery.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.BlockingBucket;
import io.github.bucket4j.Bucket;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.graphql.client.Response;
import io.smallrye.graphql.client.core.Document;
import io.smallrye.graphql.client.core.OperationType;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static io.github.bucket4j.Bandwidth.classic;
import static io.github.bucket4j.Refill.intervally;
import static io.smallrye.graphql.client.core.Argument.arg;
import static io.smallrye.graphql.client.core.Argument.args;
import static io.smallrye.graphql.client.core.Document.document;
import static io.smallrye.graphql.client.core.Enum.gqlEnum;
import static io.smallrye.graphql.client.core.Field.field;
import static io.smallrye.graphql.client.core.InputObject.inputObject;
import static io.smallrye.graphql.client.core.InputObjectField.prop;
import static io.smallrye.graphql.client.core.Operation.operation;
import static java.time.Duration.ofMinutes;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@ApplicationScoped
public class BitqueryClientImpl implements BitqueryClient {

    private static final Logger LOG = Logger.getLogger(BitqueryClientImpl.class);

    @Inject
    @GraphQLClient("bitquery")
    DynamicGraphQLClient dynamicClient;

    @ConfigProperty(name = "bitquery.api.key")
    String apiKey;

    @Inject
    @CacheName("bitquery-cache")
    Cache cache;

    private final HttpClient httpClient;

    private final BlockingBucket bucket;

    public BitqueryClientImpl() {
        httpClient = HttpClient.newBuilder().build();
        bucket = Bucket.builder()
                .addLimit(classic(10, intervally(10, ofMinutes(1))))
                .build().asBlocking();
    }

    @Override
    public double getBitcoinBalances(String network, String address) {
        String key = network + "-" + address;
        return cache.get(key, x -> {
            try {
                return bitcoinBalances(network, address);
            } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
                Thread.currentThread().interrupt();
                LOG.warnf("Unable to load balance for address %s on %s", network, address);
                return 0d;
            }
        }).await().indefinitely();
    }

    private double bitcoinBalances(String network, String address) throws ExecutionException, InterruptedException, JsonProcessingException {
        Document bitcoinDocument = document(
                operation(
                        field("bitcoin",
                                args(arg("network", gqlEnum(network))),
                                field("inputs",
                                        args(arg("inputAddress", inputObject(prop("is", address)))),
                                        field("value")
                                ),
                                field("outputs",
                                        args(arg("outputAddress", inputObject(prop("is", address)))),
                                        field("value")
                                )
                        )
                )
        );

        JsonObject data = dynamicClient.executeSync(bitcoinDocument).getData();
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
    public List<BitqueryEthereumBalance> getEthereumBalances(String network, String address) {
        String key = network + "-" + address;
        return cache.get(key, x -> ethereumBalances(network, address)).await().indefinitely();
    }

    private List<BitqueryEthereumBalance> ethereumBalances(String network, String address) {
        String rawRequest = "{\"query\":\"{  ethereum(network: " + network + ") { " +
                "address(address: {is: \\\"" + address + "\\\"}) { " +
                "balances { " +
                "currency { address symbol } value } } }}\"}";

        return executeRequest(BitqueryEthereumResponse.class, rawRequest)
                .map(response -> response.data.ethereum.address.stream()
                        .filter(x -> x.balances != null)
                        .flatMap(x -> x.balances.stream()).toList())
                .orElse(emptyList());
    }

    private <T> Optional<T> executeRequest(Class<T> clazz, String rawRequest) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(rawRequest))
                .uri(URI.create("https://graphql.bitquery.io/"))
                .setHeader("X-API-KEY", apiKey)
                .header("Content-Type", "application/json")
                .build();

        try {
            bucket.consume(1);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return Optional.of(new ObjectMapper().readValue(response.body(), clazz));
        } catch (IOException e) {
            LOG.error("BitqueryClient IOException", e);
            return Optional.empty();
        } catch (InterruptedException e) {
            LOG.error("BitqueryClient InterruptedException", e);
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }
}
