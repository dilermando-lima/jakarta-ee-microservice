package demo.queue;



import demo.activemq.ActiveMQListener;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;

public class MsgConsumer {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(MsgConsumer.class);
     
    @ActiveMQListener(destination = Const.QUEUE_NAME_1,concurrency = 2)
    public void listener(Message message) throws JMSException{
        logger.info("listener() : init ");
        logger.info(((TextMessage) message).getText());
        logger.info(message.getJMSCorrelationID());
        logger.info(message.getPropertyNames());
    }
    
}
