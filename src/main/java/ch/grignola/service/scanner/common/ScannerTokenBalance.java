package ch.grignola.service.scanner.common;

import ch.grignola.model.Allocation;
import ch.grignola.model.Network;

import java.math.BigDecimal;

public class ScannerTokenBalance {
    private final Network network;
    private final Allocation allocation;
    private final BigDecimal nativeValue;
    private final String tokenSymbol;

    public ScannerTokenBalance(Network network, Allocation allocation, BigDecimal nativeValue, String tokenSymbol) {
        this.network = network;
        this.allocation = allocation;
        this.nativeValue = nativeValue;
        this.tokenSymbol = tokenSymbol;
    }

    public Network getNetwork() {
        return network;
    }

    public Allocation getAllocation() {
        return allocation;
    }

    public BigDecimal getNativeValue() {
        return nativeValue;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }
}
