package ch.grignola.service.scanner.terra;

import ch.grignola.service.scanner.terra.client.TerraCommonRestClient;
import ch.grignola.service.scanner.terra.client.TerraRestClient;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TerraScanServiceImpl extends AbstractTerraScanService implements TerraScanService {

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
    protected TerraCommonRestClient getClient() {
        return terraRestClient;
    }
}
