package core.rest.exception;

import jakarta.ws.rs.core.Response;

public class BadRequesException extends AbstractException {

    private final Response.Status status = Response.Status.BAD_REQUEST;

    public BadRequesException(String msg){
        super(msg);
    }

    public BadRequesException(String msg,Throwable throwable){
        super(msg,throwable);
    }

    @Override
    public  Response.Status status(){
        return status;
    }
    
}
