package ch.grignola.service.token.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MarketData {

    @JsonProperty("current_price")
    public CurrentPrice currentPrice;
    @JsonProperty("total_value_locked")
    public Object totalValueLocked;
    @JsonProperty("mcap_to_tvl_ratio")
    public Object mcapToTvlRatio;
    @JsonProperty("fdv_to_tvl_ratio")
    public Object fdvToTvlRatio;
    @JsonProperty("roi")
    public Object roi;
    @JsonProperty("ath")
    public Ath ath;
    @JsonProperty("ath_change_percentage")
    public AthChangePercentage athChangePercentage;
    @JsonProperty("ath_date")
    public AthDate athDate;
    @JsonProperty("atl")
    public Atl atl;
    @JsonProperty("atl_change_percentage")
    public AtlChangePercentage atlChangePercentage;
    @JsonProperty("atl_date")
    public AtlDate atlDate;
    @JsonProperty("market_cap")
    public MarketCap marketCap;
    @JsonProperty("market_cap_rank")
    public Object marketCapRank;
    @JsonProperty("fully_diluted_valuation")
    public FullyDilutedValuation fullyDilutedValuation;
    @JsonProperty("total_volume")
    public TotalVolume totalVolume;
    @JsonProperty("high_24h")
    public High24h high24h;
    @JsonProperty("low_24h")
    public Low24h low24h;
    @JsonProperty("price_change_24h")
    public float priceChange24h;
    @JsonProperty("price_change_percentage_24h")
    public float priceChangePercentage24h;
    @JsonProperty("price_change_percentage_7d")
    public float priceChangePercentage7d;
    @JsonProperty("price_change_percentage_14d")
    public float priceChangePercentage14d;
    @JsonProperty("price_change_percentage_30d")
    public float priceChangePercentage30d;
    @JsonProperty("price_change_percentage_60d")
    public float priceChangePercentage60d;
    @JsonProperty("price_change_percentage_200d")
    public float priceChangePercentage200d;
    @JsonProperty("price_change_percentage_1y")
    public float priceChangePercentage1y;
    @JsonProperty("market_cap_change_24h")
    public float marketCapChange24h;
    @JsonProperty("market_cap_change_percentage_24h")
    public float marketCapChangePercentage24h;
    @JsonProperty("price_change_24h_in_currency")
    public PriceChange24hInCurrency priceChange24hInCurrency;
    @JsonProperty("price_change_percentage_1h_in_currency")
    public PriceChangePercentage1hInCurrency priceChangePercentage1hInCurrency;
    @JsonProperty("price_change_percentage_24h_in_currency")
    public PriceChangePercentage24hInCurrency priceChangePercentage24hInCurrency;
    @JsonProperty("price_change_percentage_7d_in_currency")
    public PriceChangePercentage7dInCurrency priceChangePercentage7dInCurrency;
    @JsonProperty("price_change_percentage_14d_in_currency")
    public PriceChangePercentage14dInCurrency priceChangePercentage14dInCurrency;
    @JsonProperty("price_change_percentage_30d_in_currency")
    public PriceChangePercentage30dInCurrency priceChangePercentage30dInCurrency;
    @JsonProperty("price_change_percentage_60d_in_currency")
    public PriceChangePercentage60dInCurrency priceChangePercentage60dInCurrency;
    @JsonProperty("price_change_percentage_200d_in_currency")
    public PriceChangePercentage200dInCurrency priceChangePercentage200dInCurrency;
    @JsonProperty("price_change_percentage_1y_in_currency")
    public PriceChangePercentage1yInCurrency priceChangePercentage1yInCurrency;
    @JsonProperty("market_cap_change_24h_in_currency")
    public MarketCapChange24hInCurrency marketCapChange24hInCurrency;
    @JsonProperty("market_cap_change_percentage_24h_in_currency")
    public MarketCapChangePercentage24hInCurrency marketCapChangePercentage24hInCurrency;
    @JsonProperty("total_supply")
    public Object totalSupply;
    @JsonProperty("max_supply")
    public Object maxSupply;
    @JsonProperty("circulating_supply")
    public float circulatingSupply;
    @JsonProperty("last_updated")
    public String lastUpdated;

}
