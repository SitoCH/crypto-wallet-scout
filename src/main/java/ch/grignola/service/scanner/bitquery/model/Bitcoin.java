package ch.grignola.service.scanner.bitquery.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bitcoin {

    @JsonProperty("inputs")
    public List<BitcoinField> inputs = null;
    @JsonProperty("outputs")
    public List<BitcoinField> outputs = null;
}
