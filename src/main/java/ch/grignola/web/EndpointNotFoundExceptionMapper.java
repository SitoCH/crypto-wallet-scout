package ch.grignola.web;

import io.quarkus.arc.Priority;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@Provider
@Priority(Priorities.USER)
public class EndpointNotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException e) {
        final InputStream indexIS = Thread.currentThread().getContextClassLoader().getResourceAsStream("index.html");
        final InputStreamReader isr = new InputStreamReader(indexIS, StandardCharsets.UTF_8);
        final StringBuilder sb = new StringBuilder();
        int c;
        try {
            while ((c = isr.read()) != -1) {
                sb.append((char) c);
            }
        } catch (IOException ex) {
            return Response.temporaryRedirect(URI.create("/")).build();
        }

        return Response.ok(sb.toString(), MediaType.TEXT_HTML_TYPE)
                .cacheControl(CacheControl.valueOf("no-cache, no-store, must-revalidate"))
                .build();
    }
}
