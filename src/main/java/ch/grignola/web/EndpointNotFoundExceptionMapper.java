package ch.grignola.web;

import io.quarkus.arc.Priority;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.Priorities;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.InputStream;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Provider
@Priority(Priorities.USER)
public class EndpointNotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    private final CacheControl cacheControl;

    public EndpointNotFoundExceptionMapper() {
        cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        cacheControl.setNoStore(true);
        cacheControl.setMustRevalidate(true);
    }

    @Override
    @Produces(MediaType.TEXT_HTML)
    public Response toResponse(NotFoundException exception) {
        InputStream resource = ClassLoader.getSystemResourceAsStream("META-INF/resources/index.html");
        return null == resource ? Response.status(NOT_FOUND).build() : Response.ok().cacheControl(cacheControl).entity(resource).build();
    }
}
