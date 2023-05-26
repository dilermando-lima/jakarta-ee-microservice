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

    private DatasourceRegister(){}

    public static synchronized void registerDatasource(DatasourceProvider datasourceProvider){
        Objects.requireNonNull(datasourceProvider);
        logger.debug("registerDatasource() : datasourceName = {}", datasourceProvider);
        datasourceProviderMap.put(datasourceProvider.datasourceName(), datasourceProvider);
    }

    public static EntityManager retrieveEntityManager(String datasourceName){ 
        logger.debug("retrieveEntityManager() : datasourceName = {}",datasourceName);
        return retrieveEntityManagerFactory(datasourceName).createEntityManager();
    }

    public static class DatasourceRegisterException extends RuntimeException {
        public DatasourceRegisterException(String msg,Throwable throwable){
            super(msg,throwable);
        }
        public DatasourceRegisterException(String msg){
            super(msg);
        }
    }

    public static synchronized EntityManagerFactory retrieveEntityManagerFactory(String datasourceName) {
        logger.debug("retrieveEntityManagerFactory() : datasourceName = {}",datasourceName);
        Objects.requireNonNull(datasourceName);

        if( !datasourceProviderMap.containsKey(datasourceName))
            throw new DatasourceRegisterException("datasource %s has not been registered".formatted(datasourceName));

        entityManagerFactoryMap.computeIfAbsent(datasourceName,k -> {
            logger.debug("retrieveEntityManagerFactory() : creaing a new EntityManagerFactory datasourceName = {}",datasourceName);
            return datasourceProviderMap.get(k).createEntityManagerFactory();
        });

        return entityManagerFactoryMap.get(datasourceName);
    }


}
