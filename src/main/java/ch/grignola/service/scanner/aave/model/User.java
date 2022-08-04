package ch.grignola.service.scanner.aave.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class User {

    @JsonProperty("unclaimedRewards")
    public long unclaimedRewards;

}
