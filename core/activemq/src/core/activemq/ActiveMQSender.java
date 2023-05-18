package core.activemq;

import java.util.Map;
import java.util.Objects;

import jakarta.jms.Connection;
import jakarta.jms.JMSException;
import jakarta.jms.Session;

public class ActiveMQSender {
    
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ActiveMQSender.class);

    private Connection connection;

    private ActiveMQSender(String connectionName) throws JMSException{
        Objects.requireNonNull(connectionName);
        this.connection = ActiveMQConnectionRegister.retrieveConnection(connectionName);
    }

    public static ActiveMQSender init(String connectionName) throws JMSException{
        return new ActiveMQSender(connectionName);
    }

    public ActiveMQSender sendToQueue(String queueName, String message, Map<String,String> header) throws Exception{
        return send(queueName, message, ActiveMQDestinationType.QUEUE, header);
    }

    public ActiveMQSender sendToQueue(String queueName, String message) throws Exception{
        return send(queueName, message, ActiveMQDestinationType.QUEUE, null);
    }

    public ActiveMQSender sendToTopic(String topicName, String message, Map<String,String> header) throws Exception{
        return send(topicName, message, ActiveMQDestinationType.TOPIC, header);
    }

    public ActiveMQSender sendToTopic(String topicName, String message) throws Exception{
        return send(topicName, message, ActiveMQDestinationType.TOPIC, null);
    }
    
    public ActiveMQSender send(String destination, String message, ActiveMQDestinationType type, Map<String,String> header) throws JMSException{

        logger.info("send() : destination = {}, message = {}, type = {} , header = {}",destination, message, type, header );
        
        Objects.requireNonNull(destination);
        Objects.requireNonNull(message);
        Objects.requireNonNull(type);
        
        try (var session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            var msg = session.createTextMessage(message);
            if( header != null ){
                header.entrySet().forEach(entry ->{
                    try {
                        msg.setStringProperty(entry.getKey(), entry.getValue());
                    } catch (JMSException e1) {
                        throw new RuntimeException(e1);
                    }
                });
            }

            session
                .createProducer( 
                    type == ActiveMQDestinationType.QUEUE 
                    ? session.createQueue(destination) 
                    : session.createTopic(destination)
                )
                .send(msg);

        } catch (JMSException e) {
            throw e;
        }

        return this; 
        
    }
    
}
