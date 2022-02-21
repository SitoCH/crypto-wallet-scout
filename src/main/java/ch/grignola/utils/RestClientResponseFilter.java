package ch.grignola.utils;

import org.jboss.logging.Logger;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import java.io.IOException;

public class RestClientResponseFilter implements ClientResponseFilter {

    private static final Logger LOG = Logger.getLogger(RestClientResponseFilter.class);

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        LOG.debugf("Invoking URL %s", requestContext.getUri());
    }
}
