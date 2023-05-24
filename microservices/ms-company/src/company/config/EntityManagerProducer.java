package company.config;

import java.util.Map;

import company.model.Company;
import core.datasource.DatasourceProvider;
import core.datasource.DatasourceProvider.ConnectionProperties;
import core.datasource.DatasourceRegister;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class EntityManagerProducer {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(EntityManagerProducer.class);
   
    public static final String DATABASE_1_NAME = "database1";

    @PostConstruct
    public void init(){
        registerDatabase1();
    }

    @Produces
    @Named(DATABASE_1_NAME)
    public EntityManager createEntityManagerDatabase1(){
        logger.debug("createEntityManagerDatabase1() : dasourceName = {}", DATABASE_1_NAME);
        return DatasourceRegister.retrieveEntityManager(DATABASE_1_NAME);
    }

    private void registerDatabase1(){
        logger.debug("registerDatabase1() : dasourceName = {}", DATABASE_1_NAME);
        DatasourceRegister.registerDatasource(
            DatasourceProvider
                .init(DATABASE_1_NAME,
                    ConnectionProperties
                        .init(
                            Map.of("hibernate.hbm2ddl.auto", org.hibernate.tool.schema.Action.CREATE)
                        )
                        .driverClassName("org.postgresql.Driver")
                        .dialect(org.hibernate.dialect.PostgreSQLDialect.class)
                        .jdbcUrl("jdbc:postgresql://localhost:5432/postgres")
                        .password("admin")
                        .username("admin")
                )
                .registerEntity(Company.class)
        );
    }
    
    
}
