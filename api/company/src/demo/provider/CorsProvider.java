package demo.provider;

import java.io.IOException;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CorsProvider implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext rep) throws IOException {
        req.getHeaders().add("Access-Control-Allow-Origin", "*");
        req.getHeaders().add("Access-Control-Allow-Credentials", "true");
        req.getHeaders().add("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
        req.getHeaders().add("Access-Control-Allow-Methods","GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}
