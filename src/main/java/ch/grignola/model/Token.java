package ch.grignola.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Token {
    private Long id;
    private String name;
    private String symbol;
    private String coinGeckoSymbol;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCoinGeckoSymbol() {
        return coinGeckoSymbol;
    }

    public void setCoinGeckoSymbol(String coinGeckoSymbol) {
        this.coinGeckoSymbol = coinGeckoSymbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Token token = (Token) o;
        return Objects.equals(id, token.id) && Objects.equals(name, token.name) && Objects.equals(symbol, token.symbol) && Objects.equals(coinGeckoSymbol, token.coinGeckoSymbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, symbol, coinGeckoSymbol);
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", coinGeckoSymbol='" + coinGeckoSymbol + '\'' +
                '}';
    }
}
