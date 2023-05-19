package core.rest.provider;

import core.rest.exception.AbstractException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class HandleExceptionProvider implements ExceptionMapper<Throwable> {

    public record ApiExceptionResponse(int status, String msg){
        public static ApiExceptionResponse buildAbstractException(Throwable exception){
            AbstractException abstractException = AbstractException.class.cast(exception);
            return new ApiExceptionResponse(
                    abstractException.status().getStatusCode(), 
                    abstractException.getMessage()
                );
        }

        public static ApiExceptionResponse buildWebApplicationException(Throwable exception){
            WebApplicationException webApplicationExceptionn = WebApplicationException.class.cast(exception);
            return new ApiExceptionResponse(
                    webApplicationExceptionn.getResponse().getStatus(), 
                    webApplicationExceptionn.getMessage()
                );
        }
    }

    @Override
    public Response toResponse(Throwable exception) {

        ApiExceptionResponse response;

        if( exception instanceof AbstractException  ){
            response = ApiExceptionResponse.buildAbstractException(exception);
        }else if( exception instanceof WebApplicationException  ){
            response = ApiExceptionResponse.buildWebApplicationException(exception);
        }else{
            response = new ApiExceptionResponse(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), exception.getMessage());
        }
        return Response.status(response.status()).entity(response).build();
    }
    
}
