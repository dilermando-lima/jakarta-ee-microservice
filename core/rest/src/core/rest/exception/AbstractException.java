package core.rest.exception;

import jakarta.ws.rs.core.Response;

public abstract class AbstractException extends RuntimeException {

    public AbstractException(String msg){
        super(msg);
    }

    public AbstractException(String msg,Throwable throwable){
        super(msg,throwable);
    }

    public abstract Response.Status status();

    public String getMessageWithStatus() {
        return  (status() == null ? Response.Status.INTERNAL_SERVER_ERROR.getStatusCode() : status().getStatusCode())  + " - " + getMessage();
    }
    
}
