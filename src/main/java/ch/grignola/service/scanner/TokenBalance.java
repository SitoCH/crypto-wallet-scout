package ch.grignola.service.scanner;

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
    @JsonProperty("symbol")
    private final String symbol;
    @JsonProperty("name")
    private final String name;

    public TokenBalance(Network network, Allocation allocation, BigDecimal nativeValue,
                        BigDecimal usdValue, String symbol, String name) {
        this.network = network;
        this.allocation = allocation;
        this.nativeValue = nativeValue;
        this.usdValue = usdValue;
        this.symbol = symbol;
        this.name = name;
    }

    public BigDecimal getUsdValue() {
        return usdValue;
    }

    public BigDecimal getNativeValue() {
        return nativeValue;
    }

    public String getSymbol() {
        return symbol;
    }
}
