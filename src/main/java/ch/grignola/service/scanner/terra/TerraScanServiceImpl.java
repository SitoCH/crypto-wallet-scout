package ch.grignola.service.scanner.terra;

import ch.grignola.model.Network;
import ch.grignola.service.scanner.terra.client.TerraCommonRestClient;
import ch.grignola.service.scanner.terra.client.TerraRestClient;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TerraScanServiceImpl extends AbstractTerraScanService implements TerraScanService {

    private static final Logger LOG = Logger.getLogger(TerraScanServiceImpl.class);

    @Inject
    @CacheName("terra-cache")
    Cache cache;

    @Inject
    @RestClient
    TerraRestClient terraRestClient;

    @Override
    protected Cache getCache() {
        return cache;
    }

    @Override
    protected Network getNetwork() {
        return Network.TERRA;
    }

    @Override
    protected TerraCommonRestClient getClient() {
        return terraRestClient;
    }

    @Override
    protected String getNativeSymbol(String symbol) {
        if (symbol.equalsIgnoreCase("uluna")) {
            return "LUNA";
        }

        LOG.infof("Found token not currently supported: %s", symbol);
        return null;
    }
}
