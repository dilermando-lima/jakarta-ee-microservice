package core.rest.exception;

import jakarta.ws.rs.core.Response;

public class UnauthorizedException extends AbstractException {

    private final Response.Status status = Response.Status.UNAUTHORIZED;

    public UnauthorizedException(String msg){
        super(msg);
    }

    public UnauthorizedException(String msg,Throwable throwable){
        super(msg,throwable);
    }

    @Override
    public Response.Status status(){
        return status;
    }
    
}
