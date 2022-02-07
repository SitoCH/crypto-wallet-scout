package ch.grignola.service.token.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoingeckoCoinMarket {

    @JsonProperty("id")
    public String id;
    @JsonProperty("symbol")
    public String symbol;
    @JsonProperty("name")
    public String name;
    @JsonProperty("image")
    public String image;
    @JsonProperty("current_price")
    public float currentPrice;
    @JsonProperty("market_cap")
    public long marketCap;
    @JsonProperty("market_cap_rank")
    public long marketCapRank;
    @JsonProperty("fully_diluted_valuation")
    public long fullyDilutedValuation;
    @JsonProperty("total_volume")
    public long totalVolume;
    @JsonProperty("high_24h")
    public float high24h;
    @JsonProperty("low_24h")
    public float low24h;
    @JsonProperty("price_change_24h")
    public float priceChange24h;
    @JsonProperty("price_change_percentage_24h")
    public float priceChangePercentage24h;
    @JsonProperty("market_cap_change_24h")
    public long marketCapChange24h;
    @JsonProperty("market_cap_change_percentage_24h")
    public float marketCapChangePercentage24h;
    @JsonProperty("circulating_supply")
    public float circulatingSupply;
    @JsonProperty("total_supply")
    public long totalSupply;
    @JsonProperty("max_supply")
    public long maxSupply;
    @JsonProperty("ath")
    public float ath;
    @JsonProperty("ath_change_percentage")
    public float athChangePercentage;
    @JsonProperty("ath_date")
    public String athDate;
    @JsonProperty("atl")
    public float atl;
    @JsonProperty("atl_change_percentage")
    public float atlChangePercentage;
    @JsonProperty("atl_date")
    public String atlDate;
    @JsonProperty("roi")
    public Object roi;
    @JsonProperty("last_updated")
    public String lastUpdated;
    @JsonProperty("price_change_percentage_24h_in_currency")
    public float priceChangePercentage24hInCurrency;
    @JsonProperty("price_change_percentage_7d_in_currency")
    public float priceChangePercentage7dInCurrency;
}