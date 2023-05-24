package company.service;

import company.config.EntityManagerProducer;
import company.model.Company;
import core.datasource.EntityManagerCommit;
import core.rest.exception.Throw;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;

@Dependent
public class CreateCompanyService {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(CreateCompanyService.class);

    @Inject
    @Named(EntityManagerProducer.DATABASE_1_NAME)
    EntityManager em;

    public record CreateCompanyRequest(String name){}
    public record CreateCompanyResponse(String id, String name){}

    public CreateCompanyResponse create(CreateCompanyRequest request){

        logger.info("create() :  request = {}", request);

        validateRequest(request);
        validateCompanyAlreadyExist(request);
        var companyModel = convertRequestToModel(request);

        EntityManagerCommit
            .init(em)
            .exec(e -> e.persist(companyModel))
            .commit();

        var response = convertModelToResponse(companyModel);
        logger.debug("create() :  response = {}", response);

        return response;
    }

    void validateRequest(CreateCompanyRequest request){
        logger.debug("validateRequest() :  request = {}", request);
        Throw.badRequest(logger, "name is required", request == null );
        Throw.badRequest(logger, "name is required", request.name() == null );
        Throw.badRequest(logger, "name cannot be empty", request.name().isBlank() );
        Throw.badRequest(logger, "name must be less than 50 caract", request.name().length() > 51 );
    }

    void validateCompanyAlreadyExist(CreateCompanyRequest request){
        logger.debug("validateCompanyAlreadyExist() :  request = {}", request);
        var alreadyExistByName = em
                .createQuery(
                    """
                        select 
                            count(*) 
                        from 
                            Company company 
                        where 
                            company.name = :name
                    """
                    , Long.class
                )
                .setMaxResults(1)
                .setParameter("name", request.name())
                .getSingleResult() > 0L;

        Throw.badRequest(logger, "company with name %s already exists".formatted(request.name()), alreadyExistByName);
    };

    Company convertRequestToModel(CreateCompanyRequest request){
        logger.debug("convertRequestToModel() :  request = {}", request);
        var company = new Company();
        company.setName(request.name());
        return company;
    }

    CreateCompanyResponse convertModelToResponse(Company companyModel){
        logger.debug("convertModelToResponse() :  companyModel = {}", companyModel);
        return new CreateCompanyResponse(companyModel.getId(), companyModel.getName());
    }
    
}
