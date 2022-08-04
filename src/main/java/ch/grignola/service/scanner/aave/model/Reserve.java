package ch.grignola.service.scanner.aave.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Reserve {

    @JsonProperty("symbol")
    public String symbol;
    @JsonProperty("decimals")
    public Integer decimals;

}
