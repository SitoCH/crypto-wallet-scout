package ch.grignola.service.scanner.cosmos.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CosmosUnboundingResult {

    @JsonProperty("delegator_address")
    public String delegatorAddress;
    @JsonProperty("validator_address")
    public String validatorAddress;
    @JsonProperty("entries")
    public List<CosmosUnboundingBalanceEntry> entries = null;

}
