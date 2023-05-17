package demo.exception;

import jakarta.ws.rs.core.Response;

public class InternalServerErrorException extends AbstractException {

    private final Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

    public InternalServerErrorException(String msg){
        super(msg);
    }

    public InternalServerErrorException(String msg,Throwable throwable){
        super(msg,throwable);
    }

    @Override
    public Response.Status status(){
        return status;
    }
    
}
