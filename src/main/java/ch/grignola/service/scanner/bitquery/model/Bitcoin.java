package ch.grignola.service.scanner.bitquery.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class Bitcoin {

    @JsonProperty("inputs")
    public List<BitcoinField> inputs = null;
    @JsonProperty("outputs")
    public List<BitcoinField> outputs = null;
}
