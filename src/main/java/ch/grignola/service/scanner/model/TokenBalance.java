package ch.grignola.service.scanner.model;

import ch.grignola.model.Network;

import java.math.BigDecimal;

public class TokenBalance {

    private final Network network;
    private final BigDecimal nativeValue;
    private final BigDecimal usdValue;
    private final String symbol;
    private final String name;

    public TokenBalance(Network network, BigDecimal nativeValue, BigDecimal usdValue, String symbol, String name) {
        this.network = network;
        this.nativeValue = nativeValue;
        this.usdValue = usdValue;
        this.symbol = symbol;
        this.name = name;
    }

    public Network getNetwork() {
        return network;
    }

    public BigDecimal getNativeValue() {
        return nativeValue;
    }

    public BigDecimal getUsdValue() {
        return usdValue;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }
}
