package ch.grignola.service.quote;

public interface TokenPriceProvider {

    double getUsdValue(String symbol);

}
