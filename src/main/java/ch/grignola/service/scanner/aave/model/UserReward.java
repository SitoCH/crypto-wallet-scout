package ch.grignola.service.scanner.aave.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class UserReward {

    @JsonProperty("reward")
    public Reward reward;
    @JsonProperty("user")
    public User user;

}
