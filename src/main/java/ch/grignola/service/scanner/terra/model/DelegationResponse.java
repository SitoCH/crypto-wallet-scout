package ch.grignola.service.scanner.terra.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DelegationResponse {

    @JsonProperty("delegation")
    public Delegation delegation;
    @JsonProperty("balance")
    public Balance balance;

}
