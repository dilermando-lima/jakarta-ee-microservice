package core.rest.exception;

import jakarta.ws.rs.core.Response;

public class AnyException extends AbstractException {

    private final Response.Status status;

    public AnyException(String msg, Response.Status status){
        super(msg);
        this.status = status;
    }

    public AnyException(String msg, Response.Status status , Throwable throwable){
        super(msg,throwable);
        this.status = status;
    }

    @Override
    public  Response.Status status(){
        return status;
    }
    
}
