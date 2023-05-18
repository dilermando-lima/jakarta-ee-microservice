package core.datasource;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class DatasourceRegister {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(DatasourceRegister.class);
   

    private static final Map<String,EntityManagerFactory> entityManagerFactoryMap = new HashMap<>();
    private static final Map<String,DatasourceProvider> datasourceProviderMap = new HashMap<>();

    public static synchronized void registerDatasource(DatasourceProvider datasourceProvider){
        Objects.requireNonNull(datasourceProvider);
        logger.debug("registerDatasource() : datasourceName = {}",datasourceProvider.datasourceName());
        datasourceProviderMap.put(datasourceProvider.datasourceName(), datasourceProvider);
    }

    public static EntityManager retrieveEntityManager(String datasourceName){ 
        logger.debug("retrieveEntityManager() : datasourceName = {}",datasourceName);
        return retrieveEntityManagerFactory(datasourceName).createEntityManager();
    }

    public static synchronized EntityManagerFactory retrieveEntityManagerFactory(String datasourceName) {
        logger.debug("retrieveEntityManagerFactory() : datasourceName = {}",datasourceName);
        Objects.requireNonNull(datasourceName);

        if( !datasourceProviderMap.containsKey(datasourceName))
            throw new RuntimeException("datasource %s has not been registered".formatted(datasourceName));

        if( !entityManagerFactoryMap.containsKey(datasourceName) ){
            logger.debug("retrieveEntityManagerFactory() : creaing a new EntityManagerFactory datasourceName = {}",datasourceName);
            entityManagerFactoryMap.put(
                datasourceName, 
                datasourceProviderMap.get(datasourceName).createEntityManagerFactory()
            );
        }
        return entityManagerFactoryMap.get(datasourceName);
    }


}
