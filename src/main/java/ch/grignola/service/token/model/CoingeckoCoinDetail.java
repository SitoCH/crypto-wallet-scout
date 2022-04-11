package ch.grignola.service.token.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
class CodeAdditionsDeletions4Weeks {

    @JsonProperty("additions")
    public Object additions;
    @JsonProperty("deletions")
    public Object deletions;

}

@JsonInclude(JsonInclude.Include.NON_NULL)
class CommunityData {

    @JsonProperty("facebook_likes")
    public Object facebookLikes;
    @JsonProperty("twitter_followers")
    public long twitterFollowers;
    @JsonProperty("reddit_average_posts_48h")
    public float redditAveragePosts48h;
    @JsonProperty("reddit_average_comments_48h")
    public float redditAverageComments48h;
    @JsonProperty("reddit_subscribers")
    public long redditSubscribers;
    @JsonProperty("reddit_accounts_active_48h")
    public long redditAccountsActive48h;
    @JsonProperty("telegram_channel_user_count")
    public Object telegramChannelUserCount;

}


@JsonInclude(JsonInclude.Include.NON_NULL)
class ConvertedLast {

    @JsonProperty("btc")
    public float btc;
    @JsonProperty("eth")
    public float eth;
    @JsonProperty("usd")
    public float usd;

}

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoingeckoCoinDetail {

    @JsonProperty("id")
    public String id;
    @JsonProperty("symbol")
    public String symbol;
    @JsonProperty("name")
    public String name;
    @JsonProperty("asset_platform_id")
    public String assetPlatformId;
    @JsonProperty("platforms")
    public Platforms platforms;
    @JsonProperty("block_time_in_minutes")
    public long blockTimeInMinutes;
    @JsonProperty("hashing_algorithm")
    public Object hashingAlgorithm;
    @JsonProperty("categories")
    public List<String> categories = null;
    @JsonProperty("public_notice")
    public Object publicNotice;
    @JsonProperty("additional_notices")
    public List<String> additionalNotices = null;
    @JsonProperty("image")
    public Image image;
    @JsonProperty("country_origin")
    public String countryOrigin;
    @JsonProperty("genesis_date")
    public Object genesisDate;
    @JsonProperty("contract_address")
    public String contractAddress;
    @JsonProperty("sentiment_votes_up_percentage")
    public Object sentimentVotesUpPercentage;
    @JsonProperty("sentiment_votes_down_percentage")
    public Object sentimentVotesDownPercentage;
    @JsonProperty("market_cap_rank")
    public Object marketCapRank;
    @JsonProperty("coingecko_rank")
    public long coingeckoRank;
    @JsonProperty("coingecko_score")
    public float coingeckoScore;
    @JsonProperty("developer_score")
    public float developerScore;
    @JsonProperty("community_score")
    public float communityScore;
    @JsonProperty("liquidity_score")
    public float liquidityScore;
    @JsonProperty("public_interest_score")
    public float publicInterestScore;
    @JsonProperty("market_data")
    public MarketData marketData;
    @JsonProperty("community_data")
    public CommunityData communityData;
    @JsonProperty("status_updates")
    public List<Object> statusUpdates = null;
    @JsonProperty("last_updated")
    public String lastUpdated;
    @JsonProperty("tickers")
    public List<Ticker> tickers = null;

}

@JsonInclude(JsonInclude.Include.NON_NULL)
class ValueByCurrency {

    @JsonProperty("usd")
    public float usd;

}

@JsonInclude(JsonInclude.Include.NON_NULL)
class Platforms {

    @JsonProperty("polygon-pos")
    public String polygonPos;

}

@JsonInclude(JsonInclude.Include.NON_NULL)
class Ticker {

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
    public ConvertedLast convertedLast;
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
