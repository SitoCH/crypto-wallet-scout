package ch.grignola.service.scanner.terra.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TerraDelegation {
    @JsonProperty("validatorName")
    public String validatorName;
    @JsonProperty("validatorAddress")
    public String validatorAddress;
    @JsonProperty("validatorStatus")
    public String validatorStatus;
    @JsonProperty("amountDelegated")
    public String amountDelegated;
    @JsonProperty("rewards")
    public List<TerraReward> rewards = null;
    @JsonProperty("totalReward")
    public String totalReward;
}