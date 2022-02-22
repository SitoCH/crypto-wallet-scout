package ch.grignola.web;

import ch.grignola.service.token.TokenProvider;
import ch.grignola.service.token.model.TokenDetail;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/api/token/")
@Produces("application/json")
public class TokenResource {

    @Inject
    TokenProvider tokenProvider;

    @GET
    @Path("/{id}")
    @Transactional
    public TokenResult getToken(@PathParam("id") String id) {
        return tokenProvider.getById(id).map(TokenResult::new).orElse(null);
    }

    public static class TokenResult {
        @JsonProperty("name")
        private final String name;
        @JsonProperty("image")
        private final String image;
        @JsonProperty("symbol")
        private final String symbol;
        @JsonProperty("usdValue")
        private final float usdValue;
        @JsonProperty("priceChange24h")
        private final float priceChange24h;
        @JsonProperty("priceChange7d")
        private final float priceChange7d;

        public TokenResult(TokenDetail tokenDetail) {
            this.name = tokenDetail.getName();
            this.image = tokenDetail.getImage();
            this.symbol = tokenDetail.getSymbol();
            this.usdValue = tokenDetail.getUsdValue();
            this.priceChange24h = tokenDetail.getPriceChangePercentage24h();
            this.priceChange7d = tokenDetail.getPriceChangePercentage7d();
        }
    }
}
