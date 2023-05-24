package company.propertie;

import core.config.ConfigBeanAttr;
import core.config.ConfigBeanPrefix;

@ConfigBeanPrefix("APP_DATASOURCE")
public record EnvDatasourceProporties(
    
    @ConfigBeanAttr("DATABASE1_JDBC_URL")
    String DATABASE1_JDBC_URL,
    
    @ConfigBeanAttr("DATABASE1_USERNAME")
    String DATABASE1_USERNAME,
    
    @ConfigBeanAttr("DATABASE1_PASS")
    String DATABASE1_PASS

){}
