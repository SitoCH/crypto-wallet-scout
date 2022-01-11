package ch.grignola.service.scanner.solana.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SolanaTokenBalance {

    @JsonProperty("tokenAddress")
    public String tokenAddress;
    @JsonProperty("tokenAmount")
    public SolanaTokenAmount tokenAmount;
    @JsonProperty("tokenAccount")
    public String tokenAccount;
    @JsonProperty("tokenName")
    public String tokenName;
    @JsonProperty("tokenIcon")
    public String tokenIcon;
    @JsonProperty("rentEpoch")
    public int rentEpoch;
    @JsonProperty("lamports")
    public int lamports;
    @JsonProperty("tokenSymbol")
    public String tokenSymbol;
    @JsonProperty("priceUsdt")
    public float priceUsdt;

}

