package company;

import org.apache.logging.log4j.Level;

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

        RestApp
            .init(App.class)
            .rootPath("/api")
            .start();



    }
}
