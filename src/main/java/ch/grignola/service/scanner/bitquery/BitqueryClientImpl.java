package ch.grignola.service.scanner.bitquery;

import ch.grignola.service.scanner.bitquery.model.BitqueryBalance;
import ch.grignola.service.scanner.bitquery.model.BitqueryResponse;
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

    public List<BitqueryBalance> getRawBalance(String network, String address) {
        String key = network + "-" + address;
        return cache.get(key, x -> getFromBitquery(network, address)).await().indefinitely();
    }

    private List<BitqueryBalance> getFromBitquery(String network, String address) {
        String rawRequest = "{\"query\":\"{  ethereum(network: " + network + ") { " +
                "address(address: {is: \\\"" + address + "\\\"}) { " +
                "balances { " +
                "currency { address symbol } value } } }}\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(rawRequest))
                .uri(URI.create("https://graphql.bitquery.io/"))
                .setHeader("X-API-KEY", apiKey)
                .header("Content-Type", "application/json")
                .build();

        try {
            bucket.consume(1);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return new ObjectMapper().readValue(response.body(), BitqueryResponse.class).data.ethereum.address.stream()
                    .flatMap(x -> x.balances.stream()).toList();
        } catch (IOException e) {
            LOG.error("BitqueryClient IOException", e);
            return emptyList();
        } catch (InterruptedException e) {
            LOG.error("BitqueryClient InterruptedException", e);
            Thread.currentThread().interrupt();
            return emptyList();
        }
    }
}
