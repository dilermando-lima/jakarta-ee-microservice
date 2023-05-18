package core.activemq;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import jakarta.jms.Connection;
import jakarta.jms.JMSException;


public class ActiveMQConnectionRegister {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ActiveMQConnectionRegister.class);
   
    private static final Map<String,Connection> startedConnectionMap = new HashMap<>();
    private static final Map<String,ActiveMQConnectionProvider> connectionProviderMap = new HashMap<>();

    public static synchronized void registerConnection(ActiveMQConnectionProvider connectionProvider){
        Objects.requireNonNull(connectionProvider);
        logger.debug("registerConnection() : connectionName = {}",connectionProvider.connectionName());
        connectionProviderMap.put(connectionProvider.connectionName(), connectionProvider);
    }

    public static synchronized Connection retrieveConnection(String connectionName) throws JMSException {
        logger.debug("retrieveConnection() : connectionName = {}", connectionName);
        Objects.requireNonNull(connectionName);

        if( !connectionProviderMap.containsKey(connectionName))
            throw new RuntimeException("connection %s has not been registered".formatted(connectionName));

        if( !startedConnectionMap.containsKey(connectionName) ){
            logger.debug("retrieveConnection() : creaing a new startedConnection connectionName = {}", connectionName);
            startedConnectionMap.put(
                connectionName, 
                connectionProviderMap.get(connectionName).startConnection()
            );
        }
        return startedConnectionMap.get(connectionName);
    }


}
