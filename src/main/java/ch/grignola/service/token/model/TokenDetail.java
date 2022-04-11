package ch.grignola.service.token.model;

import ch.grignola.model.Allocation;

public record TokenDetail(String id, String parentId, String name, String image, String symbol, float usdValue,
                          Allocation allocation, float priceChangePercentage24h, float priceChangePercentage7d) {
}
