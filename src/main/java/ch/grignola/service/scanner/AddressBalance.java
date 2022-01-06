package ch.grignola.service.scanner;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;
import java.util.Objects;

@RegisterForReflection
public final class AddressBalance {
    private final List<TokenBalance> tokenBalances;

    public AddressBalance(List<TokenBalance> tokenBalances) {
        this.tokenBalances = tokenBalances;
    }

    @JsonProperty("tokenBalances")
    public List<TokenBalance> tokenBalances() {
        return tokenBalances;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (AddressBalance) obj;
        return Objects.equals(this.tokenBalances, that.tokenBalances);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenBalances);
    }

    @Override
    public String toString() {
        return "AddressBalance[" +
                "tokenBalances=" + tokenBalances + ']';
    }


}
