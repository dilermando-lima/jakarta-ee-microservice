package demo.config;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoadConfiguration {

    private static Map<String,String> envMap = new HashMap<>();

    public static synchronized Map<String,String> envs(){
        return Collections.unmodifiableMap(envMap);
    }

    public static void loadConfiguration(String [] args){
        try {
            Properties config = new Properties();
            config.load(LoadConfiguration.class.getClassLoader().getResourceAsStream("config.properties"));
            envMap.putAll(
                config.entrySet().stream().collect(
                                Collectors.toMap(
                                    e -> e.getKey().toString(),
                                    e -> e.getValue().toString()
                                )
                            )
            );

            var mapAppVar = Stream
                .of(args == null ? new String[] {} : args)
                .collect(LoadConfigUtil.collectVarsToMap("=", "--"));

            envMap.putAll(mapAppVar);

            LoadConfigUtil.handleDeclarationsInMap(envMap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }

}
