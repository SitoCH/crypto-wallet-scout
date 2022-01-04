package ch.grignola.service.scanner;

import ch.grignola.model.Allocation;
import ch.grignola.model.Network;

import java.math.BigDecimal;

public record TokenBalance(Network network, Allocation allocation, BigDecimal nativeValue,
                           BigDecimal usdValue, String symbol, String name) {

}
