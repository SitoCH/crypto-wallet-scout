package ch.grignola.service.scanner;

import java.math.BigDecimal;

public class TokenBalance {
    private final BigDecimal balance;
    private final String symbol;

    public TokenBalance(BigDecimal balance, String symbol) {
        this.balance = balance;
        this.symbol = symbol;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getSymbol() {
        return symbol;
    }
}
