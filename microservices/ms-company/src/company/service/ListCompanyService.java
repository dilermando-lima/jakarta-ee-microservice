package company.service;

import java.util.List;

import company.producer.EntityManagerProducer;
import company.model.Company;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;

@Dependent
public class ListCompanyService {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListCompanyService.class);

    @Inject
    @Named(EntityManagerProducer.DATABASE_1_NAME)
    EntityManager em;

    public record ItemListCompanyResponse(String id, String name){}

    public List<ItemListCompanyResponse> list(){
        logger.info("list() :  init");

        var listCompanyModel = listFromDatabase();
        
        var response = convertModelToResponse(listCompanyModel);
        logger.debug("list() :  response.size = {}", response.size());

        return response;
    }

    List<Company> listFromDatabase() {
        logger.debug("listFromDatabase() : init");
        return em.createQuery(
                    """
                        select 
                            company
                        from 
                            Company company
                    """
                    , Company.class
                )
                .getResultList();
    }

    List<ItemListCompanyResponse> convertModelToResponse(List<Company> listCompanyModel){
        logger.debug("convertModelToResponse() :  listCompanyModel.size = {}", listCompanyModel.size());
        return listCompanyModel
                .stream()
                .map(companyModel -> new ItemListCompanyResponse(companyModel.getId(), companyModel.getName()))
                .toList();
    }
    
}
