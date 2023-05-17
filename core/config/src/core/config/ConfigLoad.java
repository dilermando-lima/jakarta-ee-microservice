package core.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigLoad {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ConfigLoad.class);
    

    private static Map<String,String> envMap = new HashMap<>();
    private static Map<String,Record> beanMap = new HashMap<>();

    public static class ConfigLoadBuilder{

        private ConfigLoadBuilder(String [] args, Class<?> classMain,String fileConfig){
            this.args = args;
            this.classMain = classMain;
            this.fileConfig = fileConfig;
        }

        private final String fileConfig;
        private final Class<?> classMain;
        private final String [] args;
        private List<Class<? extends Record>> listConfigBean = new ArrayList<>(0);

        public static ConfigLoadBuilder init(Class<?> classMain, String [] args){
            return init(classMain, args, null);
        }

        public static ConfigLoadBuilder init(Class<?> classMain, String [] args , String fileConfig){
            logger.debug("init() : classMain = {}, args = {}, fileConfig = {}", classMain, args, fileConfig);
            Objects.requireNonNull(fileConfig);
            Objects.requireNonNull(classMain);
            return new ConfigLoadBuilder(args, classMain, fileConfig);
        }

        public ConfigLoadBuilder registerConfig(Class<? extends Record> configBean){
            logger.debug("registerConfig() : configBean", configBean);
            Objects.requireNonNull(configBean);
            listConfigBean.add(configBean);
            return this;
        }

    }


    private static Map<String, String> collectVarFromAppArgs(String [] args){
        return Stream
                .of(args == null ? new String[] {} : args)
                .collect(ConfigUtil.collectVarsToMap("=", "--"));
    }

    private static void handleAllConfigBean(List<Class<? extends Record>> listConfigBean){
        
        listConfigBean.forEach(configBean ->{

            if( !configBean.isAnnotationPresent(ConfigBeanPrefix.class)){
                throw new RuntimeException("configBean %s must be annotated with %s".formatted(configBean.getName(),ConfigBeanPrefix.class.getName()));
            }else{
                Object [] attrValueArray = new Object[configBean.getDeclaredFields().length];
                Class<?> [] attrTypeArray = new Class<?>[configBean.getDeclaredFields().length];
                String prefix = configBean.getAnnotation(ConfigBeanPrefix.class).value();
                for (int i = 0; i < configBean.getDeclaredFields().length; i++) {
                    var attr = configBean.getDeclaredFields()[i];      
                    if(  !attr.isAnnotationPresent(ConfigBeanAttr.class) ){
                        throw new RuntimeException("attr %s in %s must be annotated with %s".formatted(attr.getName(),configBean.getName(),ConfigBeanAttr.class.getName()));
                    }else{

                        attrTypeArray[i] = attr.getType();
                        try {
                            attrValueArray[i] = 
                                ConfigUtil.handleCastingValue(
                                    envMap.get(ConfigUtil.normalizeNameEnvVar(prefix + "." + attr.getAnnotation(ConfigBeanAttr.class).value())),
                                    attrTypeArray[i]
                                );
                        } catch (Exception e) {
                            throw new RuntimeException("attr %s in %s could not be handled. %s".formatted(attr.getName(),configBean.getName(),e.getMessage()));
                        }
                    }
                }

                try {
                    beanMap.put(
                        configBean.getName(), 
                        configBean.getConstructor(attrTypeArray).newInstance(attrValueArray)
                    );
                } catch (ReflectiveOperationException | IllegalArgumentException | SecurityException e) {
                    throw new RuntimeException("configBean %s could not been loaded".formatted(configBean.getName()));
                }
            }
        });
        
    }

    private static Map<String, String> collectVarFromFileResource(Map<String, String> mapFromAppArgs, String fileNameFromConfigBuilder, Class<?> classMain){
        
        Map<String, String>  mapFromFileResource = new HashMap<>(0);
        final String fileConfigArg = "config-file";
        final String fileConfigEnv = ConfigUtil.normalizeNameEnvVar(fileConfigArg);
        final String fileConfigDefaultName = "config.properties";

        String fileName = System.getenv(fileConfigEnv);
        if( fileName == null ){
            fileName = mapFromAppArgs.get(fileConfigArg);
            if( fileName == null ){
                fileName = fileNameFromConfigBuilder;
                if( fileName == null ){
                    fileName = fileConfigDefaultName;
                }
            }
        }

        Properties config = new Properties();
        try {
            config.load(classMain.getClassLoader().getResourceAsStream(fileName));
            logger.debug("{}.properties has been found sucessfully");

            mapFromFileResource.putAll(
                config.entrySet().stream().collect(
                    Collectors.toMap(
                        e -> ConfigUtil.normalizeNameEnvVar(e.getKey().toString()),
                        e -> e.getValue().toString()
                    )
                )
            );
        } catch (IOException e) {
            logger.warn("{}.properties has been ignored or not found.",fileName);
        }
        
        return mapFromFileResource;


    }
    
    private static Map<String, String> collectVarFromEnv(){
   
        var mapVarFromEnv = new HashMap<>(System.getenv());
        mapVarFromEnv.values().removeIf(String::isBlank);

        return mapVarFromEnv;
    }

    public static void load(ConfigLoadBuilder configLoadBuilder){
        Objects.requireNonNull(configLoadBuilder);

        var mapFromAppArgs = collectVarFromAppArgs(configLoadBuilder.args);
        var mapFromFileResource = collectVarFromFileResource(mapFromAppArgs, configLoadBuilder.fileConfig, configLoadBuilder.classMain);
        var mapVarFromEnv = collectVarFromEnv();
        
        envMap.putAll(mapFromFileResource);
        envMap.putAll(mapFromAppArgs);
        envMap.putAll(mapVarFromEnv);

        ConfigUtil.handleDeclarationsInMap(envMap);

        handleAllConfigBean(configLoadBuilder.listConfigBean);
    }

    private ConfigLoad(){}

    public static synchronized Map<String,String> envAsMap(){
        return Collections.unmodifiableMap(envMap);
    }

    public static synchronized <T> T env(String name, Class<T> type){
        Objects.requireNonNull(name);
        Objects.requireNonNull(type);
        try {
            return type.cast(ConfigUtil.handleCastingValue(envMap.get(ConfigUtil.normalizeNameEnvVar(name)), type));
        } catch (Exception e) {
            throw new RuntimeException("env %s could not been retrieved. %s".formatted(name,e.getMessage()));   
        }
    }

    public static synchronized Record envAsBean(Class<? extends Record> record){
        if( record == null ) return null;
        Record config = beanMap.get(record.getName());
        if( config == null ) return null;
        return config;
    }

}
