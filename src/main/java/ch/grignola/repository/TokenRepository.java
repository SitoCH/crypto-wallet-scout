package ch.grignola.repository;

import ch.grignola.model.Token;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class TokenRepository implements PanacheRepository<Token> {

    public Optional<Token> findBySymbol(String symbol) {
        return find("symbol", symbol).stream().findFirst();
    }
}
