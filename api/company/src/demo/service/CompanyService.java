package demo.service;

import java.util.List;

import demo.datasource.EntityManagerCommit;
import demo.model.Company;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;


@Dependent
public class CompanyService {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(CompanyService.class);

    @Inject
    @Named("default")
    EntityManager em;

    public Company getById(String id){
        logger.info("getById() : id = {}",id);
        return em.find(Company.class, id);
    }


    public List<Company> list(){
        logger.info("list() : init");
        return em.createQuery("select obj from Company obj", Company.class).getResultStream().toList();
    }

    @Transactional
    public void create(Company company){
        logger.info("create() : company = {}",company);
        EntityManagerCommit
            .init(em)
            .exec((em)-> em.persist(company))
            .commit();
    }


}
