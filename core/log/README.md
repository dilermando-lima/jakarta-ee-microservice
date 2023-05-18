# CORE LOG

## Example
```java
import org.apache.logging.log4j.Level;
import core.log.LogConfig;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class App {
    
    public static void main(String[] args){

        LogConfig.init()
            .logConsolePattern("%d{HH:mm:ss.sss} %p %25.25c : %m%n")
            .logConsoleLevel("core.datasource", Level.DEBUG)
            .logConsoleLevel("test", Level.INFO)
            .configure();

        final Logger logger = LogManager.getLogger(App.class);
    
        logger.info(" ================= test info =============== ");
    }
}

```
       