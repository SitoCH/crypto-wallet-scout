package ch.grignola.service.scanner.aave.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;

@RegisterForReflection
public class UserReserf {

    @JsonProperty("reserve")
    public Reserve reserve;
    @JsonProperty("currentTotalDebt")
    public BigDecimal currentTotalDebt;

    @JsonProperty("currentATokenBalance")
    public BigDecimal currentATokenBalance;
}
