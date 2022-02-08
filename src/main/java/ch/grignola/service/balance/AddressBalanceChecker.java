package ch.grignola.service.balance;

import java.util.List;

public interface AddressBalanceChecker {

    List<TokenBalance> getBalance(String address);

    List<TokenBalance> getBalances(List<String> addresses);
}
