package ch.grignola.service.balance;

import ch.grignola.model.Allocation;
import ch.grignola.model.Network;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public final class TokenBalance {
    @JsonProperty("network")
    private final Network network;
    @JsonProperty("allocation")
    private final Allocation allocation;
    @JsonProperty("nativeValue")
    private final BigDecimal nativeValue;
    @JsonProperty("usdValue")
    private final BigDecimal usdValue;
    @JsonProperty("tokenId")
    private final String tokenId;

    public TokenBalance(Network network, Allocation allocation, BigDecimal nativeValue, BigDecimal usdValue, String tokenId) {
        this.network = network;
        this.allocation = allocation;
        this.nativeValue = nativeValue;
        this.usdValue = usdValue;
        this.tokenId = tokenId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public BigDecimal getUsdValue() {
        return usdValue;
    }
}
