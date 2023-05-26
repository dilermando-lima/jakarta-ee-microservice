package company.service;

import company.producer.EntityManagerProducer;
import company.model.Company;
import core.rest.exception.Throw;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;

@Dependent
public class GetCompanyByIdService {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(GetCompanyByIdService.class);

    @Inject
    @Named(EntityManagerProducer.DATABASE_1_NAME)
    EntityManager em;

    public record GetCompanyByIdResponse(String id, String name){}

    public GetCompanyByIdResponse getById(String id){
        logger.info("getById() :  id = {}", id);

        validateRequest(id);

        var companyModel = getByIdFromDatabase(id);
        Throw.notFound(logger,"company with id = %s not found".formatted(id), companyModel == null);
        
        var response = convertModelToResponse(companyModel);
        logger.debug("getById() :  response = {}", response);

        return response;
    }

    Company getByIdFromDatabase(String id) {
        return em.find(Company.class, id);
    }

    void validateRequest(String id){
        logger.debug("validateRequest() :  id = {}", id);
        Throw.badRequest(logger, "id is required", id == null );
        Throw.badRequest(logger, "id cannot be empty", id.isBlank() );
    }

    GetCompanyByIdResponse convertModelToResponse(Company companyModel){
        logger.debug("convertModelToResponse() :  companyModel = {}", companyModel);
        return new GetCompanyByIdResponse(companyModel.getId(), companyModel.getName());
    }
    
}
