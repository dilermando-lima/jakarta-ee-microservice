package demo.activemq;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.inject.Qualifier;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ActiveMQListener {
   String destination();
   int concurrency() default 1;
   DestinationType destinationType() default DestinationType.QUEUE;

   public enum DestinationType {
      TOPIC, QUEUE
   }
}
