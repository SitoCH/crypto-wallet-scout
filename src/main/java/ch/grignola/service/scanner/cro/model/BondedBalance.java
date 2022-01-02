package ch.grignola.service.scanner.cro.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BondedBalance {

    @JsonProperty("denom")
    public String denom;
    @JsonProperty("amount")
    public String amount;

}
