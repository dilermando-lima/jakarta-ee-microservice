package company.propertie;

import core.config.ConfigBeanAttr;
import core.config.ConfigBeanPrefix;

@ConfigBeanPrefix("APP_SERVER")
public record EnvServerProporties(

    @ConfigBeanAttr("PORT")
    Integer PORT,

    @ConfigBeanAttr("ROOT_PATH")
    String ROOT_PATH,

    @ConfigBeanAttr("HOST")
    String HOST

){}
