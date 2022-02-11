package ch.grignola.service.scanner.terra.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TerraRewardsResponse {

    @JsonProperty("rewards")
    public List<Rewards> rewards = null;
    @JsonProperty("total")
    public List<Total> total = null;

}
