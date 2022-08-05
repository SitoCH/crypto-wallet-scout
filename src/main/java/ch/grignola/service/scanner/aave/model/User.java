package ch.grignola.service.scanner.aave.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;

@RegisterForReflection
public class User {

    @JsonProperty("unclaimedRewards")
    public BigDecimal unclaimedRewards;

}
