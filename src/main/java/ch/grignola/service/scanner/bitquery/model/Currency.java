package ch.grignola.service.scanner.bitquery.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Currency {
    @JsonProperty("address")
    public String address;
    @JsonProperty("symbol")
    public String symbol;
    @JsonProperty("tokenType")
    public String tokenType;
}
