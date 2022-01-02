package ch.grignola.service.quote.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoingeckoConvertedLast {

    @JsonProperty("btc")
    public float btc;
    @JsonProperty("eth")
    public float eth;
    @JsonProperty("usd")
    public float usd;

}
