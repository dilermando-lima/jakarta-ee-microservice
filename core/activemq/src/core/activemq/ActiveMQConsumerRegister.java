package core.activemq;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import jakarta.jms.Connection;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageListener;
import jakarta.jms.Session;

public class ActiveMQConsumerRegister {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ActiveMQConsumerRegister.class);
     
    private Connection connection;

    private ActiveMQConsumerRegister(String connectionName) throws JMSException{
        Objects.requireNonNull(connectionName);
        this.connection = ActiveMQConnectionRegister.retrieveConnection(connectionName);
    }

    public static ActiveMQConsumerRegister init(String connectionName) throws JMSException{
        return new ActiveMQConsumerRegister(connectionName);
    }

    public ActiveMQConsumerRegister register(Class<?> consumer) throws Exception{

        logger.debug("register() : consumer = {} " , consumer);

        Session session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            for (Method method : consumer.getDeclaredMethods()) {

               if( method.isAnnotationPresent(ActiveMQListener.class)){

                    logger.debug("register() : listerner method found = {}.{}() " , consumer.getName(), method.getName());
                    
                    ActiveMQListener listenerConfig = method.getAnnotation(ActiveMQListener.class);

                    for(int i = 0; i < listenerConfig.concurrency(); ++i) {

                        MessageConsumer messageConsumer = session.createConsumer(
                            listenerConfig.destinationType() ==  ActiveMQDestinationType.QUEUE 
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

