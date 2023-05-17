package demo;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.glassfish.jersey.server.ResourceConfig;

import demo.activemq.ActiveMQRegisterConsumer;
import demo.activemq.ActiveMQSender;
import demo.config.LoadConfiguration;
import demo.datasource.DatasourceFactory;
import demo.log.LogConfig;
import demo.queue.Const;
import demo.queue.MsgConsumer;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.SeBootstrap;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class App  extends Application{


    public static void main(String[] args) throws Exception {

        

        // LoadConfiguration.loadConfiguration(args);

        LogConfig.init()
            .logConsolePattern("%d{HH:mm:ss.sss} %p %25.25c : %m%n")
            .logConsoleLevel("org.glassfish.jersey", Level.DEBUG)
            //.logConsoleLevel("org.apache.activemq", Level.DEBUG)
            .logConsoleLevel("demo", Level.DEBUG)
            .build();

        final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(App.class);
     
        // ActiveMQRegisterConsumer
        //     .init()
        //     .register(MsgConsumer.class);

        // var sender = ActiveMQSender.init();
        // TimeUnit.SECONDS.sleep(3);
        // sender.send(Const.QUEUE_NAME_1, "msg 1");

        // TimeUnit.SECONDS.sleep(3);
        // sender.send(Const.QUEUE_NAME_1, "msg 2",Map.of("teste_1_header","sdfsdf"));

        // TimeUnit.SECONDS.sleep(3);
        // sender.send(Const.QUEUE_NAME_1, "msg 3");

        // TimeUnit.SECONDS.sleep(3);
        // sender.send(Const.QUEUE_NAME_1, "msg 4");

        // TimeUnit.SECONDS.sleep(3);
        // sender.send(Const.QUEUE_NAME_1, "msg 5");


          
        logger.debug("LoadConfiguration.envs = {}",LoadConfiguration.envs());

        SeBootstrap.start(
            ResourceConfig
                .forApplicationClass(App.class)
                .packages("demo")
               ,
            SeBootstrap.Configuration
                .builder()
                .property(SeBootstrap.Configuration.PROTOCOL, "HTTP")
                .property(SeBootstrap.Configuration.HOST, "localhost")
                .property(SeBootstrap.Configuration.PORT, 8080).build()
        );
        Thread.currentThread().join();



    }
}