package ch.grignola.service.scanner.solana.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SolanaTokenAmount {

    @JsonProperty("amount")
    public String amount;
    @JsonProperty("decimals")
    public int decimals;

}
