package ch.grignola.service.scanner.aave.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class AaveResponse {
    @JsonProperty("userReserves")
    public List<UserReserf> userReserves = null;
    @JsonProperty("userRewards")
    public List<UserReward> userRewards = null;
}


