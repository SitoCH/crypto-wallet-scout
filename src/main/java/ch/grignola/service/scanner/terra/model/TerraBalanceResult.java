package ch.grignola.service.scanner.terra.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TerraBalanceResult {
    @JsonProperty("balance")
    public List<TerraBalance> balance = null;
    @JsonProperty("vesting")
    public List<Object> vesting = null;
    @JsonProperty("delegations")
    public List<Object> delegations = null;
    @JsonProperty("unbondings")
    public List<Object> unbondings = null;
}
