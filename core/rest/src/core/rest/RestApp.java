package core.rest;

import org.glassfish.jersey.server.ResourceConfig;

import jakarta.ws.rs.SeBootstrap;
import jakarta.ws.rs.core.Application;


public class RestApp {

    public final Class<? extends Application> appClass;
    public String rootPath = null;
    public String [] packages = null;
    public Integer port = null;
    public String host = null;

    public static RestApp init(Class<? extends Application> appClass){
        return new RestApp(appClass);
    }

    public RestApp(Class<? extends Application> appClass) {
        this.appClass = appClass;
    }

    public RestApp rootPath(String rootPath){
        this.rootPath = rootPath;
        return this;
    }

    public RestApp host(String host){
        this.host = host;
        return this;
    }

    public RestApp packages(String... packages){
        this.packages = packages;
        return this;
    }

    public RestApp port(Integer port){
        this.port = port;
        return this;
    }

    public void start() throws Exception{
        var config = ResourceConfig
                .forApplicationClass(appClass)
                .packages(packages == null ? new String[]{appClass.getPackageName()} : packages )
                .property("jersey.config.server.wadl.disableWadl", "true");

        var server = SeBootstrap.Configuration
                .builder()
                .property(SeBootstrap.Configuration.PROTOCOL, "HTTP")
                .property(SeBootstrap.Configuration.HOST, host == null ? "localhost" : host)
                .property(SeBootstrap.Configuration.PORT, port == null ? 8080 : port)
                .property(SeBootstrap.Configuration.ROOT_PATH, rootPath == null ? "/" : rootPath)
                .build();

        SeBootstrap.start(config,server);

        Thread.currentThread().join();

    }



    
}
