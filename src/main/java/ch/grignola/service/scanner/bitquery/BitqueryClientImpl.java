package ch.grignola.service.scanner.bitquery;

import ch.grignola.service.scanner.bitquery.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.BlockingBucket;
import io.github.bucket4j.Bucket;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import static io.github.bucket4j.Bandwidth.classic;
import static io.github.bucket4j.Refill.intervally;
import static java.time.Duration.ofMinutes;
import static java.util.Collections.emptyList;

@ApplicationScoped
public class BitqueryClientImpl implements BitqueryClient {

    private static final Logger LOG = Logger.getLogger(BitqueryClientImpl.class);

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
        return cache.get(key, x -> bitcoinBalances(network, address)).await().indefinitely();
    }

    private double bitcoinBalances(String network, String address) {
        String rawRequest = "{\"query\":\"{  bitcoin(network: " + network + ") { " +
                "inputs(inputAddress: {is: \\\"" + address + "\\\"}) { value } " +
                "outputs(outputAddress: {is: \\\"" + address + "\\\"}) { value }  }}\"}";

        return executeRequest(BitqueryBitcoinResponse.class, rawRequest)
                .map(response -> getTotalBitcoins(response.data.bitcoin))
                .orElse(0d);
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
