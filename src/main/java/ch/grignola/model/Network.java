package ch.grignola.model;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public enum Network {
    POLYGON(of("polygon-pos")),
    AVALANCHE(empty()),
    TERRA(empty()),
    TERRA_CLASSIC(empty()),
    CRONOS(empty()),
    SOLANA(empty()),
    COSMOS(empty()),
    BITCOIN(empty()),
    POLKADOT(empty()),
    OPTIMISM(of("optimistic-ethereum")),
    ETHEREUM(empty()),
    BNB(of("binance-smart-chain")),
    DOGECOIN(empty());

    private final Optional<String> coingeckoNetworkId;

    Network(Optional<String> coingeckoNetworkId) {
        this.coingeckoNetworkId = coingeckoNetworkId;
    }

    public Optional<String> getCoingeckoNetworkId() {
        return coingeckoNetworkId;
    }
}
