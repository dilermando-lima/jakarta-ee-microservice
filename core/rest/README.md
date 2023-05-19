# CORE REST

## required dependencies

```
dependencies {
   implementation project(':core-rest')
   implementation project(':core-log')
}

```

## Example
```java

import core.rest.RestApp;
import jakarta.ws.rs.core.Application;

public class App extends Application{

    public static void main(String[] args) throws Exception  {
        RestApp
            .init(App.class)
            // .port(8021)          // optional default 8080
            // .host("0.0.0.0")     // optional default localhost
            // .packages("package.resources")  // option default App.class root package
            .rootPath("/api")       // optional default /
            .start();
    }

}

```
       