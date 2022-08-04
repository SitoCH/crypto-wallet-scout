package ch.grignola.service.scanner.aave.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AaveResponse {
    @JsonProperty("userReserves")
    public List<UserReserf> userReserves = null;
    @JsonProperty("userRewards")
    public List<UserReward> userRewards = null;
}


