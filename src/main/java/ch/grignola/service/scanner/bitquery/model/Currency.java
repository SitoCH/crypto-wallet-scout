package ch.grignola.service.scanner.bitquery.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Currency {
    @JsonProperty("address")
    public String address;
    @JsonProperty("symbol")
    public String symbol;
}
