package ch.grignola.service.scanner.terra.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Balance {

    @JsonProperty("denom")
    public String denom;
    @JsonProperty("amount")
    public String amount;

}
