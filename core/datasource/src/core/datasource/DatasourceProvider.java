package core.datasource;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;

import com.zaxxer.hikari.hibernate.HikariConnectionProvider;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

public class DatasourceProvider {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(DatasourceProvider.class);
  

    private final Map<String,Object> properties;
    private final String datasourceName;
    private final List<Class<?>> managedClassList = new ArrayList<>();

    public String datasourceName(){
        return datasourceName;
    }


    public static DatasourceProvider init(String datasourceName, ConnectionProperties connectionProperties){
        logger.debug("init() : datasourceName = {}, connectionProperties = {}",datasourceName,connectionProperties);
        return new DatasourceProvider(datasourceName, connectionProperties);
    }

    private DatasourceProvider(String datasourceName, ConnectionProperties connectionProperties){
        Objects.requireNonNull(datasourceName);
        Objects.requireNonNull(connectionProperties);
        this.datasourceName = datasourceName;
        this.properties = connectionProperties.getConnectionPropertiesMap();
    }

    public DatasourceProvider registerEntity(Class<?> entity){
        Objects.requireNonNull(entity);
        managedClassList.add(entity);
        return this;
    }

    public DatasourceProvider registerEntity(Class<?>... entity){
        Objects.requireNonNull(entity);
        Stream.of(entity).forEach(this::registerEntity);
        return this;
    }

    public EntityManagerFactory createEntityManagerFactory() {
        logger.debug("createEntityManagerFactory() : datasourceName = {} , managedClassList.size = {}",datasourceName,managedClassList.size());
        return new HibernatePersistenceProvider()
                    .createContainerEntityManagerFactory(
                        getUnitInfo(),
                        properties
                    );
    }

    public static class ConnectionProperties{
        private Map<String,Object> connectionPropertiesMap = defaultConnectionProperties();

        @Override
        public String toString() {
            var mapToString = new HashMap<>(connectionPropertiesMap);
            mapToString.keySet().removeIf(k -> k.contains(".password"));
            return super.toString();
        }

        private ConnectionProperties(Map<String,Object> properties){
            this.connectionPropertiesMap = defaultConnectionProperties();
            if( properties != null )
                this.connectionPropertiesMap.putAll(properties);
        }

        public static ConnectionProperties init(){
            return new ConnectionProperties(null);
        }

        public static ConnectionProperties init(Map<String,Object> connectionPropertiesMap){
            return new ConnectionProperties(connectionPropertiesMap);
        }

        public ConnectionProperties driverClassName(Object driverClassName){
            connectionPropertiesMap.put("hibernate.hikari.driverClassName", driverClassName);
            return this;
        }

        public ConnectionProperties jdbcUrl(Object jdbcUrl){
            connectionPropertiesMap.put("hibernate.hikari.jdbcUrl", jdbcUrl);
            return this;
        }

        public ConnectionProperties username(Object username){
            connectionPropertiesMap.put("hibernate.hikari.username", username);
            return this;
        }

        public ConnectionProperties password(Object password){
            connectionPropertiesMap.put("hibernate.hikari.password", password);
            return this;
        }

        public ConnectionProperties dialect(Object dialect){
            connectionPropertiesMap.put("hibernate.dialect", dialect);
            return this;
        }

        private Map<String,Object>  getConnectionPropertiesMap(){
            return connectionPropertiesMap;
        }
    }

    private static Map<String,Object> defaultConnectionProperties(){
        Map<String, Object> map = new HashMap<>();
        map.put("hibernate.hbm2ddl.auto", org.hibernate.tool.schema.Action.NONE);
        map.put("hibernate.show_sql", "false");
        map.put("hibernate.format_sql", "false");
        map.put("hibernate.connection.provider_class", HikariConnectionProvider.class);
        map.put("hibernate.hikari.maximumPoolSize","3");
        map.put("hibernate.hikari.maxLifetime", "15000");
        map.put("hibernate.hikari.minimumIdle", "5");
        map.put("hibernate.hikari.autoCommit", "false");
        return map;
    }

    private PersistenceUnitInfo getUnitInfo() {
        return new PersistenceUnitInfo() {

            @Override
            public String getPersistenceUnitName() {
                return datasourceName;
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
                return managedClassList.stream().map(Class::getName).toList();
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
