package ch.grignola.service.scanner;

import ch.grignola.service.scanner.model.AddressBalance;

public interface AddressBalanceChecker {
    AddressBalance getAddressBalance(String address);
}
