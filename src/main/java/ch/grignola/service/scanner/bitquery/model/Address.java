package ch.grignola.service.scanner.bitquery.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class Address {
    @JsonProperty("balances")
    public List<BitqueryBalance> balances = null;

}
