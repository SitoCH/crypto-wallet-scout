package ch.grignola.service.scanner.etherscan.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EthereumTokenEventResult {
    @JsonProperty("blockNumber")
    public String blockNumber;
    @JsonProperty("timeStamp")
    public long timeStamp;
    @JsonProperty("contractAddress")
    public String contractAddress;
    @JsonProperty("tokenName")
    public String tokenName;
    @JsonProperty("tokenSymbol")
    public String tokenSymbol;
    @JsonProperty("tokenDecimal")
    public int tokenDecimal;
}
