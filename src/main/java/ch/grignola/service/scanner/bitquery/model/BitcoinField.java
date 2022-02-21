package ch.grignola.service.scanner.bitquery.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BitcoinField {

    @JsonProperty("value")
    public double value;

}
