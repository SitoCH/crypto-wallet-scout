package ch.grignola.service.scanner.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EthereumTokenEventResult {
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

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public void setTokenDecimal(String tokenDecimal) {
        this.tokenDecimal = tokenDecimal;
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
