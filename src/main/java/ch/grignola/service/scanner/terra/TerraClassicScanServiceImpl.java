package ch.grignola.service.scanner.terra;

import ch.grignola.model.Network;
import ch.grignola.service.scanner.terra.client.TerraClassicRestClient;
import ch.grignola.service.scanner.terra.client.TerraCommonRestClient;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TerraClassicScanServiceImpl extends AbstractTerraScanService implements TerraClassicScanService {

    private static final Logger LOG = Logger.getLogger(TerraClassicScanServiceImpl.class);

    @Inject
    @CacheName("terra-classic-cache")
    Cache cache;

    @Inject
    @RestClient
    TerraClassicRestClient terraRestClient;

    @Override
    protected Cache getCache() {
        return cache;
    }

    @Override
    protected Network getNetwork() {
        return Network.TERRA_CLASSIC;
    }

    @Override
    protected TerraCommonRestClient getClient() {
        return terraRestClient;
    }

    @Override
    protected String getNativeSymbol(String symbol) {
        if (symbol.equalsIgnoreCase("uluna")) {
            return "LUNC";
        }

        if (symbol.equalsIgnoreCase("uusd")) {
            return "USTC";
        }

        LOG.infof("Found token not currently supported: %s", symbol);
        return null;
    }
}
