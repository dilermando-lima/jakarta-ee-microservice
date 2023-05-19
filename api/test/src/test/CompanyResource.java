package test;

import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/company")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompanyResource {


   @GET
   @Path("/{id}")
   public Response getById(@PathParam("id") String id){
      return Response.ok(id).build();
   }

   @GET
   public Response list(){
      return Response.ok(List.of("sdf","asdf")).build();
   }

   @POST
   public Response create(){
      return Response.noContent().build();
   }

}
