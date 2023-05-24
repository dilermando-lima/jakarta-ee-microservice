package company;

import org.apache.logging.log4j.Level;

import company.propertie.EnvDatasourceProporties;
import company.propertie.EnvServerProporties;
import core.config.ConfigLoad;
import core.config.ConfigLoad.ConfigLoadBuilder;
import core.log.LogConfig;
import core.rest.RestApp;
import jakarta.ws.rs.core.Application;

public class App extends Application{

    public static void main(String[] args) throws Exception {

        LogConfig
            .init()
            .logConsolePattern("%d{HH:mm:ss.sss} %p %25.25c : %m%n")
            .logConsoleLevel("company", Level.DEBUG)
            .logConsoleLevel("core", Level.DEBUG)
            .logConsoleLevel("org.glassfish", Level.DEBUG)
            .configure();

        ConfigLoad.load(
            ConfigLoadBuilder
                .init(App.class, args)
                .registerConfig(EnvDatasourceProporties.class)
                .registerConfig(EnvServerProporties.class)
        );


        var envServer = ConfigLoad.envAsBean(EnvServerProporties.class);
        RestApp
            .init(App.class)
            .rootPath(envServer.ROOT_PATH())
            .port(envServer.PORT())
            .host(envServer.HOST())
            .start();



    }
}
