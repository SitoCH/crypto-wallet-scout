package ch.grignola.service.scanner.cro.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {

    @JsonProperty("type")
    public String type;
    @JsonProperty("name")
    public String name;
    @JsonProperty("address")
    public String address;
    @JsonProperty("balance")
    public List<Balance> balance = null;
    @JsonProperty("bondedBalance")
    public List<BondedBalance> bondedBalance = null;
    @JsonProperty("redelegatingBalance")
    public List<Object> redelegatingBalance = null;
    @JsonProperty("unbondingBalance")
    public List<Object> unbondingBalance = null;
    @JsonProperty("totalRewards")
    public List<TotalReward> totalRewards = null;
    @JsonProperty("commissions")
    public List<Object> commissions = null;
    @JsonProperty("totalBalance")
    public List<TotalBalance> totalBalance = null;

}
