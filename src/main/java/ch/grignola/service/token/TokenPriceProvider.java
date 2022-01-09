package ch.grignola.service.token;

public interface TokenPriceProvider {

    double getUsdValue(String symbol);

}
