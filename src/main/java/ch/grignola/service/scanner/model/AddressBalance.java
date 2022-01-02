package ch.grignola.service.scanner.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class AddressBalance {

    private final List<TokenBalance> tokenBalances;

    public AddressBalance(List<TokenBalance> tokenBalances) {
        this.tokenBalances = tokenBalances;
    }

    public List<TokenBalance> getTokenBalances() {
        return tokenBalances;
    }
}
