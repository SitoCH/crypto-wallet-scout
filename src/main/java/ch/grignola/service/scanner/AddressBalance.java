package ch.grignola.service.scanner;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public record AddressBalance(List<TokenBalance> tokenBalances) {

}
