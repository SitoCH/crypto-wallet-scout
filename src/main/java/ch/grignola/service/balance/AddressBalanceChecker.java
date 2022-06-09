package ch.grignola.service.balance;

import java.util.List;

public interface AddressBalanceChecker {

    List<TokenBalance> getBalance(String address);

    TokenBalances getBalances(List<String> addresses);
}
