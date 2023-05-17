package demo.exception;

import jakarta.ws.rs.core.Response;

public class NotFoundException extends AbstractException {

    private final Response.Status status = Response.Status.NOT_FOUND;

    public NotFoundException(String msg){
        super(msg);
    }

    public NotFoundException(String msg,Throwable throwable){
        super(msg,throwable);
    }

    @Override
    public Response.Status status(){
        return status;
    }
    
}
