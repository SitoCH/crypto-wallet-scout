package ch.grignola.service.scanner.polygon;

import ch.grignola.service.scanner.AddressBalance;
import ch.grignola.service.scanner.TokenBalance;
import ch.grignola.utils.DistinctByKey;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.rightPad;

@ApplicationScoped
public class PolygonScanServiceImpl implements PolygonScanService {

    private static final Logger LOG = Logger.getLogger(PolygonScanServiceImpl.class);

    @Inject
    @RestClient
    PolygonScanRestClient polygonScanRestClient;

    @ConfigProperty(name = "polygonscan.api.key")
    String apiKey;

    public AddressBalance getAddressBalance(String address) {
        LOG.infof("Loading Polygon address balance %s", address);
        List<TokenBalance> tokenBalances = polygonScanRestClient.getPolygonTokenEvents(apiKey, "tokentx", address).getResult()
                .stream()
                .filter(new DistinctByKey<>(PolygonScanRestClient.Result::getContractAddress)::filterByKey)
                .map(x -> toAddressBalance(address, x, polygonScanRestClient.getPolygonTokenBalance(apiKey, "tokenbalance", address, x.getContractAddress())))
                .filter(x -> x.getBalance().compareTo(BigDecimal.ZERO) != 0)
                .collect(toList());

        return new AddressBalance(tokenBalances);
    }

    private TokenBalance toAddressBalance(String address, PolygonScanRestClient.Result tokenEvent, PolygonScanRestClient.PolygonTokenBalanceResult tokenBalance) {
        LOG.infof("Polygon token balance for address %s based on event %s: %s", address, tokenEvent, tokenBalance.getResult());
        BigDecimal balance = new BigDecimal(tokenBalance.getResult()).divide((new BigDecimal(rightPad("1", parseInt(tokenEvent.getTokenDecimal()) + 1, '0'))), MathContext.DECIMAL64);
        return new TokenBalance(balance, tokenEvent.getTokenSymbol());
    }

}
