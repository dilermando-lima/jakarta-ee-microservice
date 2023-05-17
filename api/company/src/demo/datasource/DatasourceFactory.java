package demo.datasource;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;

import com.zaxxer.hikari.hibernate.HikariConnectionProvider;

import demo.model.Company;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

public class DatasourceFactory {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(DatasourceFactory.class);

    private static EntityManagerFactory entityManagerFactoryDefault = null;

    public static synchronized EntityManagerFactory createEntityManagerFactoryDefault() {
        logger.info("createEntityManagerFactoryDefault() : datasourceBuilder = defaultDatasource");
        if( entityManagerFactoryDefault == null ){
            entityManagerFactoryDefault = new HibernatePersistenceProvider()
                    .createContainerEntityManagerFactory(
                        unitInfo(),
                        defaultConfig()
                    );
        }
        return entityManagerFactoryDefault;
    }

    public static synchronized EntityManager createEntityManagerDefault(){
        logger.debug("createEntityManagerDefault() : datasourceBuilder = defaultDatasource");
        return createEntityManagerFactoryDefault().createEntityManager();
    }

    public static Map<String,Object> defaultConfig(){
        Map<String, Object> map = new HashMap<>();

        map.put("hibernate.hbm2ddl.auto", org.hibernate.tool.schema.Action.CREATE);
        map.put("hibernate.dialect", org.hibernate.dialect.PostgreSQLDialect.class);
        map.put("hibernate.show_sql", "false");
        map.put("hibernate.format_sql", "false");
        map.put("hibernate.connection.provider_class", HikariConnectionProvider.class);
        map.put("hibernate.connection.provider_disables_autocommit", false);        

        map.put("hibernate.hikari.driverClassName", "org.postgresql.Driver");
        map.put("hibernate.hikari.jdbcUrl", "jdbc:postgresql://localhost:5432/postgres");
        map.put("hibernate.hikari.username", "admin");
        map.put("hibernate.hikari.password","admin");
        map.put("hibernate.hikari.maximumPoolSize","10");
        map.put("hibernate.hikari.maxLifetime", "1500000");
        map.put("hibernate.hikari.minimumIdle", "5");
        map.put("hibernate.hikari.autoCommit", "false");
        
        return map;
    }

    public static PersistenceUnitInfo unitInfo() {
        return new PersistenceUnitInfo() {

            @Override
            public String getPersistenceUnitName() {
                return "teste-unit"; // DefaultDatasource.class.getName();
            }

            @Override
            public String getPersistenceProviderClassName() {
                return null;
            }

            @Override
            public PersistenceUnitTransactionType getTransactionType() {
                return PersistenceUnitTransactionType.RESOURCE_LOCAL;
            }

        
            @Override
            public List<String> getMappingFileNames() {
                return null;
            }

            @Override
            public List<URL> getJarFileUrls() {
                return null;
            }

            @Override
            public URL getPersistenceUnitRootUrl() {
                return null;
            }

            @Override
            public List<String> getManagedClassNames() {
                return List.of(Company.class.getName());
            }

            @Override
            public boolean excludeUnlistedClasses() {
                return false;
            }

            @Override
            public SharedCacheMode getSharedCacheMode() {
                return null;
            }

            @Override
            public ValidationMode getValidationMode() {
                return null;
            }

            @Override
            public Properties getProperties() {
                return null;
            }

            @Override
            public String getPersistenceXMLSchemaVersion() {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {
                return null;
            }

            @Override
            public void addTransformer(ClassTransformer transformer) {
            }

            @Override
            public ClassLoader getNewTempClassLoader() {
                return null;
            }

            @Override
            public DataSource getJtaDataSource() {
                return null;
            }

            @Override
            public DataSource getNonJtaDataSource() {
                return null;
            }
        };

    }
    
}
