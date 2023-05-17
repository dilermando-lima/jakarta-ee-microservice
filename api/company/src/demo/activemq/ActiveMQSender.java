package demo.activemq;

import java.util.Map;

import jakarta.jms.Connection;
import jakarta.jms.JMSException;
import jakarta.jms.Session;

public class ActiveMQSender {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ActiveMQSender.class);
 

    private Connection connection;

    private ActiveMQSender(Connection connection) throws JMSException{
        if( connection == null ){
            this.connection = ActiveMQConnectionProducer.defaultConnection();
        }else{
            this.connection = connection;
        }
    }

    public static ActiveMQSender init(Connection connection) throws JMSException{
        return new ActiveMQSender(connection);
    }

    public static ActiveMQSender init() throws JMSException{
        return init(null);
    }

    public ActiveMQSender send(String destination, String message) throws Exception{
        return send(destination, message, null);
    }
    
    public ActiveMQSender send(String destination, String message,Map<String,String> header) throws Exception{

        logger.info("send() : destination = {}, message = {} ",destination, message);

        var session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

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
            .createProducer(session.createQueue(destination))
            //.createProducer(session.createTopic(message))
            .send(msg);

        session.close();

        return this;

 
        
    }
    
}
