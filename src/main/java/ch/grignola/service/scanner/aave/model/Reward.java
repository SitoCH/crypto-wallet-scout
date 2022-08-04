package ch.grignola.service.scanner.aave.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Reward {

    @JsonProperty("rewardTokenSymbol")
    public String rewardTokenSymbol;
    @JsonProperty("rewardTokenDecimals")
    public int rewardTokenDecimals;
}
