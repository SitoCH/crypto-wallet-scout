package ch.grignola.service.scanner.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EthereumTokenEventResult {
    @JsonProperty("blockNumber")
    public String blockNumber;
    @JsonProperty("timeStamp")
    public String timeStamp;
    @JsonProperty("hash")
    public String hash;
    @JsonProperty("nonce")
    public String nonce;
    @JsonProperty("blockHash")
    public String blockHash;
    @JsonProperty("from")
    public String from;
    @JsonProperty("contractAddress")
    public String contractAddress;
    @JsonProperty("to")
    public String to;
    @JsonProperty("value")
    public String value;
    @JsonProperty("tokenName")
    public String tokenName;
    @JsonProperty("tokenSymbol")
    public String tokenSymbol;
    @JsonProperty("tokenDecimal")
    public String tokenDecimal;
    @JsonProperty("transactionIndex")
    public String transactionIndex;
    @JsonProperty("gas")
    public String gas;
    @JsonProperty("gasPrice")
    public String gasPrice;
    @JsonProperty("gasUsed")
    public String gasUsed;
    @JsonProperty("cumulativeGasUsed")
    public String cumulativeGasUsed;
    @JsonProperty("input")
    public String input;
    @JsonProperty("confirmations")
    public String confirmations;

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
