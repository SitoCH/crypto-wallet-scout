package ch.grignola.service.scanner.cosmos.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CosmosStackedBalanceResult {

    @JsonProperty("delegation")
    public Delegation delegation;
    @JsonProperty("balance")
    public Balance balance;

}
