package ch.grignola.service.scanner.aave.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Reserve {

    @JsonProperty("symbol")
    public String symbol;
    @JsonProperty("decimals")
    public Integer decimals;

}
