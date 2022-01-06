package ch.grignola.service.scanner;

import ch.grignola.model.Allocation;
import ch.grignola.model.Network;

import java.math.BigDecimal;
import java.util.Objects;

public final class TokenBalance {
    private final Network network;
    private final Allocation allocation;
    private final BigDecimal nativeValue;
    private final BigDecimal usdValue;
    private final String symbol;
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

    public Network network() {
        return network;
    }

    public Allocation allocation() {
        return allocation;
    }

    public BigDecimal nativeValue() {
        return nativeValue;
    }

    public BigDecimal usdValue() {
        return usdValue;
    }

    public String symbol() {
        return symbol;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TokenBalance) obj;
        return Objects.equals(this.network, that.network) &&
                Objects.equals(this.allocation, that.allocation) &&
                Objects.equals(this.nativeValue, that.nativeValue) &&
                Objects.equals(this.usdValue, that.usdValue) &&
                Objects.equals(this.symbol, that.symbol) &&
                Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(network, allocation, nativeValue, usdValue, symbol, name);
    }

    @Override
    public String toString() {
        return "TokenBalance[" +
                "network=" + network + ", " +
                "allocation=" + allocation + ", " +
                "nativeValue=" + nativeValue + ", " +
                "usdValue=" + usdValue + ", " +
                "symbol=" + symbol + ", " +
                "name=" + name + ']';
    }


}
