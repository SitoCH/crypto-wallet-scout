package ch.grignola.service.scanner.terra.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rewards {

    @JsonProperty("validator_address")
    public String validatorAddress;
    @JsonProperty("reward")
    public List<Reward> reward = null;

}
