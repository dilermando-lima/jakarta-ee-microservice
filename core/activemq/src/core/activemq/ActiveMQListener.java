package core.activemq;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ActiveMQListener {
   String destination();
   int concurrency() default 1;
   ActiveMQDestinationType destinationType() default ActiveMQDestinationType.QUEUE;
}

