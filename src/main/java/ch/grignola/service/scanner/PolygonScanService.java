package ch.grignola.service.scanner;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import java.util.List;

@RegisterRestClient(baseUri = "https://api.polygonscan.com/api?module=account&tag=latest")
public interface PolygonScanService {

    @GET
    PolygonTokenEventsResult getPolygonTokenEventsResult(@QueryParam("apikey") String apikey,
                                                         @QueryParam("action") String action,
                                                         @QueryParam("address") String address);

    @JsonInclude(JsonInclude.Include.NON_NULL)
    class PolygonTokenEventsResult {

        @JsonProperty("status")
        private String status;
        @JsonProperty("message")
        private String message;
        @JsonProperty("result")
        private List<Result> result = null;

        public List<Result> getResult() {
            return result;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    class Result {

        @JsonProperty("blockNumber")
        private String blockNumber;
        @JsonProperty("timeStamp")
        private String timeStamp;
        @JsonProperty("hash")
        private String hash;
        @JsonProperty("nonce")
        private String nonce;
        @JsonProperty("blockHash")
        private String blockHash;
        @JsonProperty("from")
        private String from;
        @JsonProperty("contractAddress")
        private String contractAddress;
        @JsonProperty("to")
        private String to;
        @JsonProperty("value")
        private String value;
        @JsonProperty("tokenName")
        private String tokenName;
        @JsonProperty("tokenSymbol")
        private String tokenSymbol;
        @JsonProperty("tokenDecimal")
        private String tokenDecimal;
        @JsonProperty("transactionIndex")
        private String transactionIndex;
        @JsonProperty("gas")
        private String gas;
        @JsonProperty("gasPrice")
        private String gasPrice;
        @JsonProperty("gasUsed")
        private String gasUsed;
        @JsonProperty("cumulativeGasUsed")
        private String cumulativeGasUsed;
        @JsonProperty("input")
        private String input;
        @JsonProperty("confirmations")
        private String confirmations;

        public String getTokenName() {
            return tokenName;
        }
    }
}
