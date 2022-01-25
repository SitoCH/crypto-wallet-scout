package ch.grignola.service.scanner.cosmos.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CosmosRewardsBalanceResult {

    @JsonProperty("rewards")
    public List<Rewards> rewards = null;
    @JsonProperty("total")
    public List<Total> total = null;

}
