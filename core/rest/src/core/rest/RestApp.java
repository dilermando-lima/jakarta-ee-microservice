package core.rest;

import java.util.stream.Stream;

import org.glassfish.jersey.server.ResourceConfig;

import jakarta.ws.rs.SeBootstrap;
import jakarta.ws.rs.core.Application;


public class RestApp {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(RestApp.class);
 

    private final Class<? extends Application> appClass;
    private String rootPath = null;
    private String [] packages = null;
    private Integer port = null;
    private String host = null;

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

    public static class RestAppException extends RuntimeException {
        public RestAppException(String msg,Throwable throwable){
            super(msg,throwable);
        }
        public RestAppException(String msg){
            super(msg);
        }
    }

    public void start() throws RestAppException{

    
        final String protocolo = "HTTP"; 
        packages = packages == null ? new String[]{appClass.getPackageName()} : packages;
        host = host == null ? "localhost" : host;
        port = port == null ? 8080 : port;
        rootPath = rootPath == null ? "/" : rootPath;


        packages = Stream
                    .concat(
                        Stream.of(packages), 
                        Stream.of(RestApp.class.getPackageName())
                    ).toArray(String[]::new);

        logger.info("start() : appClass = {} endpoint = {}:{}{} ",appClass,host,port,rootPath);
        logger.debug("start() : packages = {}",(Object) packages);

        var config = ResourceConfig
                .forApplicationClass(appClass)
                .setClassLoader(appClass.getClassLoader())
                .packages(packages)
                .property("jersey.config.server.wadl.disableWadl", "true")
                .property("jersey.config.server.provider.classnames", "org.glassfish.jersey.jackson.JacksonFeature");


        var server = SeBootstrap.Configuration
                .builder()
                .property(SeBootstrap.Configuration.PROTOCOL, protocolo)
                .property(SeBootstrap.Configuration.HOST, host)
                .property(SeBootstrap.Configuration.PORT, port)
                .property(SeBootstrap.Configuration.ROOT_PATH, rootPath)
                .build();

        logger.debug("start() : Starting server");

        SeBootstrap.start(config,server);

        logger.debug("start() : Server has been started successfully");

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            logger.error(e);
            Thread.currentThread().interrupt();
            throw new RestAppException(protocolo, e);
        }

    }



    
}
