package company.resource;

import company.service.CreateCompanyService;
import company.service.GetCompanyByIdService;
import company.service.ListCompanyService;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
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

    @Inject Instance<CreateCompanyService> createCompanyService;
    @Inject Instance<GetCompanyByIdService> getCompanyByIdService;
    @Inject Instance<ListCompanyService> listCompanyService;

    @POST
    public Response create(CreateCompanyService.CreateCompanyRequest company){
       return Response.ok(createCompanyService.get().create(company)).status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id){
       return Response.ok(getCompanyByIdService.get().getById(id)).build();
    }

    @GET
    public Response list(){
       return Response.ok(listCompanyService.get().list()).build();
    }

}
