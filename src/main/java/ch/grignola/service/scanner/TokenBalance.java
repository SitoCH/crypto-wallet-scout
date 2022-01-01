package ch.grignola.service.scanner;

import ch.grignola.model.Network;

import java.math.BigDecimal;

public class TokenBalance {

    private final Network network;
    private final BigDecimal balance;
    private final String symbol;
    private final String name;

    public TokenBalance(Network network, BigDecimal balance, String symbol, String name) {
        this.network = network;
        this.balance = balance;
        this.symbol = symbol;
        this.name = name;
    }

    public Network getNetwork() {
        return network;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }
}
