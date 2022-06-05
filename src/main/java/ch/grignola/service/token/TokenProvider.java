package ch.grignola.service.token;

import ch.grignola.model.Network;
import ch.grignola.service.token.model.TokenDetail;

import java.util.Optional;

public interface TokenProvider {

    Optional<TokenDetail> getBySymbol(String symbol);

    Optional<TokenDetail> getById(String tokenId);

    void refreshCache();

    TokenContract getContract(Network network, String contractAddress);
}
