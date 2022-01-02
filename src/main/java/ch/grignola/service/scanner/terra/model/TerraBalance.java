package ch.grignola.service.scanner.terra.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TerraBalance {
    @JsonProperty("denom")
    public String denom;
    @JsonProperty("available")
    public BigInteger available;
    @JsonProperty("delegatedVesting")
    public String delegatedVesting;
    @JsonProperty("delegatable")
    public String delegatable;
    @JsonProperty("freedVesting")
    public String freedVesting;
    @JsonProperty("unbonding")
    public String unbonding;
    @JsonProperty("remainingVesting")
    public String remainingVesting;
}
