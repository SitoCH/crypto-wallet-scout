package ch.grignola.service.scanner.bitquery;

import ch.grignola.service.scanner.bitquery.model.Balance;
import ch.grignola.service.scanner.bitquery.model.BitqueryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static java.util.Collections.emptyList;

@ApplicationScoped
public class BitqueryClientImpl implements BitqueryClient {

    private static final Logger LOG = Logger.getLogger(BitqueryClientImpl.class);

    @ConfigProperty(name = "bitquery.api.key")
    String apiKey;

    private final HttpClient httpClient;

    public BitqueryClientImpl() {
        this.httpClient = HttpClient.newBuilder().build();
    }

    public List<Balance> getRawBalance(String network, String address) {
        String rawRequest = "{\"query\":\"{  ethereum(network: " + network + ") { " +
                "address(address: {is: \\\"" + address + "\\\"}) { " +
                "balances { " +
                "currency { address symbol tokenType } value } } }}\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(rawRequest))
                .uri(URI.create("https://graphql.bitquery.io/"))
                .setHeader("X-API-KEY", apiKey)
                .header("Content-Type", "application/json")
                .build();

        try {
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
