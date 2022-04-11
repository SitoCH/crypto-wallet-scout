package ch.grignola.service.scanner.common;

import ch.grignola.model.Allocation;
import ch.grignola.model.Network;

import java.math.BigDecimal;

public record ScannerTokenBalance(Network network, Allocation allocation, BigDecimal nativeValue, String tokenSymbol) {
}
