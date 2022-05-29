package ch.grignola.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class TerraTokenContract {
    private Long id;
    private String contractId;
    private String symbol;
    private Long decimals;
    private Network network;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getDecimals() {
        return decimals;
    }

    public void setDecimals(Long decimals) {
        this.decimals = decimals;
    }

    @Enumerated(EnumType.STRING)
    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TerraTokenContract that = (TerraTokenContract) o;
        return Objects.equals(id, that.id) && Objects.equals(contractId, that.contractId) && Objects.equals(symbol, that.symbol) && Objects.equals(decimals, that.decimals) && network == that.network;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contractId, symbol, decimals, network);
    }
}
