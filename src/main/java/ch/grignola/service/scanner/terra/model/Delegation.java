package ch.grignola.service.scanner.terra.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Delegation {

    @JsonProperty("delegator_address")
    public String delegatorAddress;
    @JsonProperty("validator_address")
    public String validatorAddress;
    @JsonProperty("shares")
    public String shares;

}
