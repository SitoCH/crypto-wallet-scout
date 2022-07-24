package ch.grignola.service.scanner.solana.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SolanaTokenBalance {

    @JsonProperty("tokenAmount")
    public SolanaTokenAmount tokenAmount;
    @JsonProperty("tokenSymbol")
    public String tokenSymbol;

}

