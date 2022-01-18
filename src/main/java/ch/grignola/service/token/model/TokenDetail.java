package ch.grignola.service.token.model;

public class TokenDetail {
    private final String id;
    private final String image;
    private final String name;
    private final String symbol;
    private final Float usdValue;
    private final float priceChangePercentage24h;
    private final float priceChangePercentage7d;

    public TokenDetail(String id, String name, String image, String symbol, float usdValue, float priceChangePercentage24h, float priceChangePercentage7d) {
        this.id = id;
        this.usdValue = usdValue;
        this.image = image;
        this.name = name;
        this.symbol = symbol;
        this.priceChangePercentage24h = priceChangePercentage24h;
        this.priceChangePercentage7d = priceChangePercentage7d;
    }

    public String getId() {
        return id;
    }

    public Float getUsdValue() {
        return usdValue;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public float getPriceChangePercentage24h() {
        return priceChangePercentage24h;
    }

    public float getPriceChangePercentage7d() {
        return priceChangePercentage7d;
    }
}
