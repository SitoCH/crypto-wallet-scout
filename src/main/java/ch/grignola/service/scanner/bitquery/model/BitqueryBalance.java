package ch.grignola.service.scanner.bitquery.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class BitqueryBalance {
    @JsonProperty("value")
    public double value;
    @JsonProperty("currency")
    public Currency currency;

}
