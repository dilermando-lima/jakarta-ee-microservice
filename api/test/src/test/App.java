package test;

import org.apache.logging.log4j.Level;

import core.log.LogConfig;

public class App {
    
    public static void main(String[] args){

        
       
        LogConfig.init()
        .logConsolePattern("%d{HH:mm:ss.sss} %p %25.25c : %m%n")
        .logConsoleLevel("org.glassfish.jersey", Level.DEBUG)
        //.logConsoleLevel("org.apache.activemq", Level.DEBUG)
        .logConsoleLevel("demo", Level.DEBUG)
        .build();

        final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(App.class);
 

    }

   


}
