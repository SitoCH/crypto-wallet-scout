package ch.grignola.service.scanner.cosmos.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Reward {

    @JsonProperty("denom")
    public String denom;
    @JsonProperty("amount")
    public String amount;

}
