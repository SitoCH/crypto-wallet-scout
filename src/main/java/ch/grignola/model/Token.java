package ch.grignola.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Token {
    private Long id;
    private String name;
    private String symbol;
    private String coinGeckoId;
    private String parentId;
    private Allocation allocation;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(unique = true, nullable = false)
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCoinGeckoId() {
        return coinGeckoId;
    }

    public void setCoinGeckoId(String coinGeckoId) {
        this.coinGeckoId = coinGeckoId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Enumerated(EnumType.STRING)
    public Allocation getAllocation() {
        return allocation;
    }

    public void setAllocation(Allocation allocation) {
        this.allocation = allocation;
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
        return Objects.equals(id, token.id) && Objects.equals(name, token.name) && Objects.equals(symbol, token.symbol) && Objects.equals(coinGeckoId, token.coinGeckoId) && Objects.equals(parentId, token.parentId) && allocation == token.allocation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, symbol, coinGeckoId, parentId, allocation);
    }
}
