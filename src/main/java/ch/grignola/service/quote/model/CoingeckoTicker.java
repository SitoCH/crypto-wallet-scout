package ch.grignola.service.quote.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoingeckoTicker {

    @JsonProperty("base")
    public String base;
    @JsonProperty("target")
    public String target;
    @JsonProperty("market")
    public Market market;
    @JsonProperty("last")
    public float last;
    @JsonProperty("volume")
    public float volume;
    @JsonProperty("converted_last")
    public CoingeckoConvertedLast convertedLast;
    @JsonProperty("converted_volume")
    public ConvertedVolume convertedVolume;
    @JsonProperty("trust_score")
    public String trustScore;
    @JsonProperty("bid_ask_spread_percentage")
    public float bidAskSpreadPercentage;
    @JsonProperty("timestamp")
    public String timestamp;
    @JsonProperty("last_traded_at")
    public String lastTradedAt;
    @JsonProperty("last_fetch_at")
    public String lastFetchAt;
    @JsonProperty("is_anomaly")
    public boolean isAnomaly;
    @JsonProperty("is_stale")
    public boolean isStale;
    @JsonProperty("trade_url")
    public String tradeUrl;
    @JsonProperty("token_info_url")
    public Object tokenInfoUrl;
    @JsonProperty("coin_id")
    public String coinId;
    @JsonProperty("target_coin_id")
    public String targetCoinId;

}
