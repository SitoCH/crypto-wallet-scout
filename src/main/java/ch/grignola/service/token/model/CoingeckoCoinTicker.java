package ch.grignola.service.token.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
class ConvertedVolume {

    @JsonProperty("btc")
    public float btc;
    @JsonProperty("eth")
    public float eth;
    @JsonProperty("usd")
    public float usd;

}

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoingeckoCoinTicker {

    @JsonProperty("name")
    public String name;
    @JsonProperty("tickers")
    public List<CoingeckoTicker> tickers = null;

}

@JsonInclude(JsonInclude.Include.NON_NULL)
class Market {

    @JsonProperty("name")
    public String name;
    @JsonProperty("identifier")
    public String identifier;
    @JsonProperty("has_trading_incentive")
    public boolean hasTradingIncentive;

}

