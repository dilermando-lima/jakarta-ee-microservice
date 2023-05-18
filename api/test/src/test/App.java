package test;



import org.apache.logging.log4j.Level;

import core.datasource.DatasourceProvider;
import core.datasource.DatasourceProvider.ConnectionProperties;
import core.log.LogConfig;
import core.datasource.DatasourceRegister;
import core.datasource.EntityManagerCommit;
import jakarta.persistence.EntityManager;

public class App {
    

    public static void main(String[] args){

        LogConfig.init()
            .logConsolePattern("%d{HH:mm:ss.sss} %p %25.25c : %m%n")
            .logConsoleLevel("core.datasource", Level.DEBUG)
            .logConsoleLevel("test", Level.INFO)
            .configure();

        final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(DatasourceProvider.class);
    
        logger.info(" ================= teste info =============== ");

        DatasourceRegister.registerDatasource(
            DatasourceProvider
                .init("datasource1", 
                    ConnectionProperties
                        .init()
                        .driverClassName("org.postgresql.Driver")
                        .dialect(org.hibernate.dialect.PostgreSQLDialect.class)
                        .jdbcUrl("jdbc:postgresql://localhost:5432/postgres")
                        .password("admin")
                        .username("admin")
                )
                .registerEntity(Company.class)
        );

        EntityManager em = DatasourceRegister.retrieveEntityManager("datasource1");
   
        EntityManagerCommit
            .init(em)
            .exec((e)-> e.persist(new Company("name 1")))
            .exec((e)-> e.persist(new Company("name 1")))
            .commit();

        System.out.println(em.createQuery("select obj from test.Company obj", Company.class).getResultStream().toList());

       
    }

   


}
