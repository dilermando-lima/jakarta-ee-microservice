package demo.resource;

import demo.model.Company;
import demo.service.CompanyService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/company")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompanyResource {

   @Inject
   CompanyService service;

   @GET
   @Path("/{id}")
   public Response getById(@PathParam("id") String id){
      return Response.ok(service.getById(id)).build();
   }

   @GET
   public Response list(){
      return Response.ok(service.list()).build();
   }

   @POST
   public Response create(Company company){
      service.create(company);
      return Response.noContent().build();
   }

   @GET
   @Produces(MediaType.TEXT_PLAIN)
   @Path("/{name}")
   public String doGreeting(@PathParam("name") String someValue, @QueryParam("language") String language) {
      return "Hello " + someValue + " with language " + language;
   }
}
