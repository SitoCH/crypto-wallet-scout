package ch.grignola.service.scanner.aave.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @JsonProperty("unclaimedRewards")
    public long unclaimedRewards;

}
