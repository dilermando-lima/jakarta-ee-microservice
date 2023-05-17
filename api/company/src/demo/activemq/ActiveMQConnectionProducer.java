package demo.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import jakarta.jms.Connection;
import jakarta.jms.JMSException;

public class ActiveMQConnectionProducer {

    private static Connection connectionDefault = null;

    public static synchronized Connection defaultConnection() throws JMSException{

        if( connectionDefault == null ){

            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
    
            factory.setBrokerURL("failover:(tcp://localhost:61618)?jms.prefetchPolicy.queuePrefetch=1&jms.rmIdFromConnectionId=true&initialReconnectDelay=2000&maxReconnectAttempts=2");
            factory.setUserName("admin");
            factory.setPassword("admin");
    
            connectionDefault = factory.createConnection();
        }
        
        connectionDefault.start();

        return connectionDefault;
    }



    
}
