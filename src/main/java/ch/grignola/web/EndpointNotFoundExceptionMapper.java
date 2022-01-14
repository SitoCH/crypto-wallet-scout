package ch.grignola.web;

import io.quarkus.arc.Priority;
import org.jboss.resteasy.annotations.cache.Cache;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.Priorities;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.InputStream;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Provider
@Priority(Priorities.USER)
public class EndpointNotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    @Produces(MediaType.TEXT_HTML)
    @Cache(noCache = true, noStore = true, mustRevalidate = true)
    public Response toResponse(NotFoundException exception) {
        InputStream resource = ClassLoader.getSystemResourceAsStream("META-INF/resources/index.html");
        return null == resource ? Response.status(NOT_FOUND).build() : Response.ok().entity(resource).build();
    }
}
