package core.activemq;

import java.util.Objects;

import org.apache.activemq.ActiveMQConnectionFactory;

import jakarta.jms.Connection;
import jakarta.jms.JMSException;



public class ActiveMQConnectionProvider {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ActiveMQConnectionProvider.class);
  
    private final String connectionName;
    private final ConnectionProperties connectionProperties;

    public String connectionName(){
        return connectionName;
    }

    public static ActiveMQConnectionProvider init(String datasourceName, ConnectionProperties connectionProperties){
        logger.debug("init() : datasourceName = {}, connectionProperties = {}",datasourceName,connectionProperties);
        return new ActiveMQConnectionProvider(datasourceName, connectionProperties);
    }

    private ActiveMQConnectionProvider(String datasourceName, ConnectionProperties connectionProperties){
        Objects.requireNonNull(datasourceName);
        Objects.requireNonNull(connectionProperties);
        this.connectionName = datasourceName;
        this.connectionProperties = connectionProperties;
    }

    public Connection startConnection() throws JMSException {
        logger.debug("createConnectionFactory() : datasourceName = {}",connectionName);
        
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(connectionProperties.url);
        factory.setUserName(connectionProperties.username);
        factory.setPassword(connectionProperties.password);

        Connection connection =  factory.createConnection();
        connection.start();
        return connection;
    }

    public static class ConnectionProperties{
        private final String url;
        private final String username;
        private final String password;

        @Override
        public String toString() {
            return "ConnectionProperties [url=" + url + ", username=" + username + "]";
        }

        public ConnectionProperties(String url, String username, String password) {
            Objects.requireNonNull(url);
            Objects.requireNonNull(username);
            Objects.requireNonNull(password);
            this.url = url;
            this.username = username;
            this.password = password;
        }

        public static ConnectionProperties init(String url, String username, String password){
            return new ConnectionProperties(url, username, password);
        }
    }
    
}
