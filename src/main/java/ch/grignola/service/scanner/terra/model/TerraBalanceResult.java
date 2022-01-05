package ch.grignola.service.scanner.terra.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TerraBalanceResult {
    @JsonProperty("redelegations")
    public List<Object> redelegations = null;
    @JsonProperty("delegationTotal")
    public String delegationTotal;
    @JsonProperty("undelegations")
    public List<Object> undelegations = null;
    @JsonProperty("rewards")
    public TerraRewards rewards;
    @JsonProperty("myDelegations")
    public List<TerraDelegation> myDelegations = null;
    @JsonProperty("availableLuna")
    public String availableLuna;
}
