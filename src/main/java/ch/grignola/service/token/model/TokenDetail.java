package ch.grignola.service.token.model;

public record TokenDetail(String id, String parentId, String name, String image, String symbol, float usdValue,
                          float priceChangePercentage24h, float priceChangePercentage7d,
                          float priceChangePercentage30d, float priceChangePercentage200d) {
}
