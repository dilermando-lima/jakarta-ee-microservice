package demo.activemq;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import demo.activemq.ActiveMQListener.DestinationType;
import jakarta.jms.Connection;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageListener;
import jakarta.jms.Session;

public class ActiveMQRegisterConsumer {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ActiveMQRegisterConsumer.class);
     
    private Connection connection;

    private ActiveMQRegisterConsumer(Connection connection) throws JMSException{
        if( connection == null ){
            this.connection = ActiveMQConnectionProducer.defaultConnection();
        }else{
            this.connection = connection;
        }
    }

    public static ActiveMQRegisterConsumer init(Connection connection) throws JMSException{
        return new ActiveMQRegisterConsumer(connection);
    }

    public static ActiveMQRegisterConsumer init() throws JMSException{
        return init(null);
    }


    public ActiveMQRegisterConsumer register(Class<?> consumer) throws JMSException{

        logger.debug("register() : consumer = {} " , consumer);

        Session session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        
        for (Method method : consumer.getDeclaredMethods()) {

           if( method.isAnnotationPresent(ActiveMQListener.class)){

                logger.debug("register() : listerner method found = {}.{}() " , consumer.getName(), method.getName());
                
                ActiveMQListener listenerConfig = method.getAnnotation(ActiveMQListener.class);

                for(int i = 0; i < listenerConfig.concurrency(); ++i) {

                    MessageConsumer messageConsumer = session.createConsumer(
                        listenerConfig.destinationType() ==  DestinationType.QUEUE 
                            ? session.createQueue(listenerConfig.destination())
                            : session.createTopic(listenerConfig.destination())
                    );
                    
                    messageConsumer.setMessageListener(new MessageListener() {
                        @Override
                        public void onMessage(Message message) {
                            try {
                                method.invoke(consumer.getConstructor().newInstance(), message);
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                                    | InstantiationException | NoSuchMethodException | SecurityException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
           }
            
        }

        return this;
    }


    
}
