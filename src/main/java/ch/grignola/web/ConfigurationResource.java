package ch.grignola.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/public/api/configuration")
@Produces("application/json")
public class ConfigurationResource {

    @ConfigProperty(name = "quarkus.oidc.auth-server-url")
    String authServerUrl;

    @ConfigProperty(name = "quarkus.oidc.client-id")
    String clientId;

    @GET
    public Configuration getConfiguration() {
        return new Configuration(authServerUrl, clientId);
    }

    public static class Configuration {
        @JsonProperty("authServerUrl")
        private final String authServerUrl;
        @JsonProperty("clientId")
        private final String clientId;

        private Configuration(String authServerUrl, String clientId) {
            this.authServerUrl = authServerUrl;
            this.clientId = clientId;
        }
    }
}
