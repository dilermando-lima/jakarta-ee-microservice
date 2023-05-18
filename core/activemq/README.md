# CORE ACTIVEMQ

## required dependencies

```
dependencies {
   implementation project(':core-activemq')
   implementation project(':core-log')
}

```

## Example
```java
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;

import core.activemq.ActiveMQConnectionProvider;
import core.activemq.ActiveMQConnectionProvider.ConnectionProperties;
import core.activemq.ActiveMQConnectionRegister;
import core.activemq.ActiveMQConsumerRegister;
import core.activemq.ActiveMQListener;
import core.activemq.ActiveMQSender;
import core.log.LogConfig;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;

public class App {

    public static final String NAME_CONNECTION1 = "CONECTION1";
    public static final String NAME_QUEUE1 = "QUEUE1";

    // declare a consumer listener to be register later
    public static class ConsumerListener {
        private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager
                .getLogger(ConsumerListener.class);

        @ActiveMQListener(destination = NAME_CONNECTION1)
        // @ActiveMQListener(destination = NAME_CONNECTION1, concurrency = 2,
        // destinationType = ActiveMQDestinationType.QUEUE )
        public void listener(Message message) throws JMSException {
            logger.info("listener() : init ");
            logger.info(((TextMessage) message).getText());
            logger.info(message.getJMSCorrelationID());
            logger.info(message.getPropertyNames());
        }
    }

    public static void main(String[] args) throws JMSException, Exception {

        LogConfig.init()
                .logConsolePattern("%d{HH:mm:ss.sss} %p %25.25c : %m%n")
                .logConsoleLevel("core.activemq", Level.DEBUG)
                .logConsoleLevel("test", Level.INFO)
                .configure();
        // register a connection
        ActiveMQConnectionRegister
                .registerConnection(
                        ActiveMQConnectionProvider.init(
                                NAME_CONNECTION1,
                                ConnectionProperties.init(
                                        "tcp://localhost:61618",
                                        "admin",
                                        "admin")));

        // register a consumer listener
        ActiveMQConsumerRegister
                .init(NAME_CONNECTION1)
                .register(ConsumerListener.class);

        // send a message
        var sender = ActiveMQSender.init(NAME_CONNECTION1);
        TimeUnit.SECONDS.sleep(3);
        sender.sendToQueue(NAME_QUEUE1, "mensage 1");

        // send a message
        TimeUnit.SECONDS.sleep(3);
        sender.sendToQueue(NAME_QUEUE1, "mensage 2", Map.of("HEADER1", "value-1"));

    }

}

```
       