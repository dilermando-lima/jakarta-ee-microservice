# CORE DATASOURCE

## Example
```java

import core.datasource.DatasourceProvider;
import core.datasource.DatasourceProvider.ConnectionProperties;
import core.datasource.DatasourceRegister;
import core.datasource.EntityManagerCommit;
import jakarta.persistence.EntityManager;

public class App {
    public static void main(String[] args){
        // we have to register a datasource once at the begin
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

        // we can retrieve a entityManager whenever we need
        EntityManager em = DatasourceRegister.retrieveEntityManager("datasource1");
   
        EntityManagerCommit
            .init(em)
            .exec((e)-> e.persist(new Company("name 1")))
            .exec((e)-> e.persist(new Company("name 1")))
            .commit();

        System.out.println(
            em.createQuery(
                "select obj from test.Company obj", 
                Company.class
            )
            .getResultStream()
            .toList()
        );
    }
}

```
       