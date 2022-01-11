package ch.grignola.service.token;

public interface TokenProvider {

    String getImageSmall(String symbol);

    double getUsdValue(String symbol);

}
