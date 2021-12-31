package ch.grignola.service.scanner.polygon;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import java.util.List;

@RegisterRestClient(baseUri = "https://api.polygonscan.com/api?module=account&tag=latest")
public interface PolygonScanRestClient {

    @GET
    PolygonTokenEventsResult getPolygonTokenEvents(@QueryParam("apikey") String apikey,
                                                   @QueryParam("action") String action,
                                                   @QueryParam("address") String address);

    @GET
    PolygonTokenBalanceResult getPolygonTokenBalance(@QueryParam("apikey") String apikey,
                                                     @QueryParam("action") String action,
                                                     @QueryParam("address") String address,
                                                     @QueryParam("contractaddress") String contractAddress);

    @JsonInclude(JsonInclude.Include.NON_NULL)
    class PolygonTokenBalanceResult {

        @JsonProperty("status")
        private String status;
        @JsonProperty("message")
        private String message;
        @JsonProperty("result")
        private String result;

        public String getResult() {
            return result;
        }
    }

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

        public String getContractAddress() {
            return contractAddress;
        }

        public String getTokenName() {
            return tokenName;
        }

        public String getTokenSymbol() {
            return tokenSymbol;
        }

        public String getTokenDecimal() {
            return tokenDecimal;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "blockNumber='" + blockNumber + '\'' +
                    ", timeStamp='" + timeStamp + '\'' +
                    ", hash='" + hash + '\'' +
                    ", nonce='" + nonce + '\'' +
                    ", blockHash='" + blockHash + '\'' +
                    ", from='" + from + '\'' +
                    ", contractAddress='" + contractAddress + '\'' +
                    ", to='" + to + '\'' +
                    ", value='" + value + '\'' +
                    ", tokenName='" + tokenName + '\'' +
                    ", tokenSymbol='" + tokenSymbol + '\'' +
                    ", tokenDecimal='" + tokenDecimal + '\'' +
                    ", transactionIndex='" + transactionIndex + '\'' +
                    ", gas='" + gas + '\'' +
                    ", gasPrice='" + gasPrice + '\'' +
                    ", gasUsed='" + gasUsed + '\'' +
                    ", cumulativeGasUsed='" + cumulativeGasUsed + '\'' +
                    ", input='" + input + '\'' +
                    ", confirmations='" + confirmations + '\'' +
                    '}';
        }
    }
}
