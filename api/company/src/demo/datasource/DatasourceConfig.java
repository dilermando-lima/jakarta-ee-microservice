package demo.datasource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class DatasourceConfig {

    @Produces
    @Named("default")
    public EntityManager entityManagerDefault(){
        return DatasourceFactory.createEntityManagerDefault();
    }
    
}
