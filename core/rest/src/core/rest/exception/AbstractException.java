package core.rest.exception;

import jakarta.ws.rs.core.Response;

public abstract class AbstractException extends RuntimeException {

    protected AbstractException(String msg){
        super(msg);
    }

    protected AbstractException(String msg,Throwable throwable){
        super(msg,throwable);
    }

    public abstract Response.Status status();

    public String getMessageWithStatus() {
        return  (status() == null ? Response.Status.INTERNAL_SERVER_ERROR.getStatusCode() : status().getStatusCode())  + " - " + getMessage();
    }
    
}
