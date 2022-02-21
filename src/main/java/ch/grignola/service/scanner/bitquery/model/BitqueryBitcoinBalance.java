package ch.grignola.service.scanner.bitquery.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class BitqueryBitcoinBalance {
    @JsonProperty("bitcoin")
    public Bitcoin bitcoin;
}
