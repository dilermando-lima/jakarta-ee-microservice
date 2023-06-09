package core.rest.exception;

import jakarta.ws.rs.core.Response;

public class Throw {
    private Throw(){}

    public static void any(org.apache.logging.log4j.Logger logger, Response.Status status, String msg){
            var exception = new AnyException(msg, status);
            logger.error(exception.getMessageWithStatus());
            throw exception;
    }
    
    public static void badRequest(org.apache.logging.log4j.Logger logger, String msg, boolean condition){
        if( condition ){
            var exception = new BadRequesException(msg);
            logger.error(exception.getMessageWithStatus());
            throw exception;
        }
    }
    
    public static void forbidden(org.apache.logging.log4j.Logger logger, String msg, boolean condition){
        if( condition ){
            var exception = new ForbidenException(msg);
            logger.error(exception.getMessageWithStatus());
            throw exception;
        }
    }

    public static void notFound(org.apache.logging.log4j.Logger logger, String msg, boolean condition){
        if( condition ){
            var exception = new NotFoundException(msg);
            logger.error(exception.getMessageWithStatus());
            throw exception;
        }
    }

    public static void unauthorized(org.apache.logging.log4j.Logger logger, String msg, boolean condition){
        if( condition ){
            var exception = new UnauthorizedException(msg);
            logger.error(exception.getMessageWithStatus());
            throw exception;
        }
    }

    public static void internalServer(org.apache.logging.log4j.Logger logger, String msg, boolean condition){
        if( condition ){
            var exception = new InternalServerErrorException(msg);
            logger.error(exception.getMessageWithStatus());
            throw exception;
        }
    }

    public static void internalServer(org.apache.logging.log4j.Logger logger, Throwable throwable, String msg, boolean condition ){
        if( condition ){
            var exception = new InternalServerErrorException(msg, throwable);
            logger.error(exception.getMessageWithStatus());
            throw exception;
        }
    }
}
