package demo.exception;

import jakarta.ws.rs.core.Response;

public class ForbidenException extends AbstractException {

    private final Response.Status status = Response.Status.FORBIDDEN;

    public ForbidenException(String msg){
        super(msg);
    }

    public ForbidenException(String msg,Throwable throwable){
        super(msg,throwable);
    }

    @Override
    public Response.Status status(){
        return status;
    }
    
}
