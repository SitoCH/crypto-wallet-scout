package ch.grignola.service.scanner.terra;

import ch.grignola.service.scanner.terra.client.TerraClassicRestClient;
import ch.grignola.service.scanner.terra.client.TerraCommonRestClient;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TerraClassicScanServiceImpl extends AbstractTerraScanService implements TerraClassicScanService {
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
    protected TerraCommonRestClient getClient() {
        return terraRestClient;
    }
}
