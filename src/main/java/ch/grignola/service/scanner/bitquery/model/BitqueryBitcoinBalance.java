package ch.grignola.service.scanner.bitquery.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BitqueryBitcoinBalance {
    @JsonProperty("bitcoin")
    public Bitcoin bitcoin;
}

