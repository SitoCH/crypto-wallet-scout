package ch.grignola.service.scanner.terra.client;

import ch.grignola.utils.RestClientResponseFilter;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterProvider(RestClientResponseFilter.class)
@RegisterRestClient(baseUri = "https://phoenix-lcd.terra.dev")
public interface TerraRestClient extends TerraCommonRestClient {

}
