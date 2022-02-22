package ch.grignola.service.scanner.polkadot.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Balances {

    @JsonProperty("accountId")
    public String accountId;
    @JsonProperty("accountNonce")
    public String accountNonce;
    @JsonProperty("additional")
    public List<Object> additional = null;
    @JsonProperty("freeBalance")
    public String freeBalance;
    @JsonProperty("frozenFee")
    public String frozenFee;
    @JsonProperty("frozenMisc")
    public String frozenMisc;
    @JsonProperty("reservedBalance")
    public String reservedBalance;
    @JsonProperty("votingBalance")
    public String votingBalance;
    @JsonProperty("availableBalance")
    public String availableBalance;
    @JsonProperty("lockedBalance")
    public String lockedBalance;
    @JsonProperty("lockedBreakdown")
    public List<Object> lockedBreakdown = null;
    @JsonProperty("vestingLocked")
    public String vestingLocked;
    @JsonProperty("isVesting")
    public boolean isVesting;
    @JsonProperty("vestedBalance")
    public String vestedBalance;
    @JsonProperty("vestedClaimable")
    public String vestedClaimable;
    @JsonProperty("vesting")
    public List<Object> vesting = null;
    @JsonProperty("vestingTotal")
    public String vestingTotal;

}
