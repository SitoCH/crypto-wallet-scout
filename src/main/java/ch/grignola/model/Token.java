package ch.grignola.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
}
