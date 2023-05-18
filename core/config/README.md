# CORE CONFIG

## required dependencies

```
dependencies {
    implementation project(':core-config') // this project
    implementation project(':core-log')
}
```


## Example

Using file config in resource. `config.properties` added at resouces
```
prefix1.var-text-1=value-text-1
prefix1.var-number-1=3
same-var=teste asdf  asdf asdf  asd  asd f
```
> file properties in resource is optional and is not required to add it

Example aplicattion loading vars from application argumentes, environment variavables and properties file.
```java

import core.config.ConfigBeanAttr;
import core.config.ConfigBeanPrefix;
import core.config.ConfigLoad;
import core.config.ConfigLoad.ConfigLoadBuilder;

public class App {

    // create a bean use when loading variables ( optional )
    @ConfigBeanPrefix("prefix1")
    public record BeamProperties(

        @ConfigBeanAttr("var-text-1")
        String varNumber,

        @ConfigBeanAttr("VAR_NUMBER_1")
        Integer varText

    ){}

    public static void main(String[] args)  {

        // load all configutation
        ConfigLoad.load(
            ConfigLoadBuilder.init(
                App.class, 
                args, 
                "config.properties" // name of properties file
            )
            .registerConfig(BeamProperties.class) // registering properties bean
        );

        // example reading vars loadded
        System.out.println(ConfigLoad.env("prefix1.var-text-1",String.class));
        System.out.println(ConfigLoad.env("PREFIX1_VAR_TEXT_1",String.class));
        System.out.println(ConfigLoad.env("PREFIX1_VAR_NUMBER_1",Integer.class));

        System.out.println(ConfigLoad.envAsBean(BeamProperties.class));

        System.out.println(ConfigLoad.envAsMap());

    }

}

```
       