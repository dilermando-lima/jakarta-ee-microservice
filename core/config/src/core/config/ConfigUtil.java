package core.config;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigUtil {

    private ConfigUtil() {
    }

    public static Object handleCastingValue(String value, Class<?> type) throws Exception{

        if(CastType.STRING.equals(type)) return CastType.STRING.handler.apply(value);
        if(CastType.INTEGER.equals(type)) return CastType.INTEGER.handler.apply(value);
        if(CastType.BOOLEAN.equals(type)) return CastType.BOOLEAN.handler.apply(value);
        if(CastType.FLOAT.equals(type)) return CastType.FLOAT.handler.apply(value);
        if(CastType.DOUBLE.equals(type)) return CastType.DOUBLE.handler.apply(value);
        if(CastType.LONG.equals(type)) return CastType.LONG.handler.apply(value);
        if(CastType.SHORT.equals(type)) return CastType.SHORT.handler.apply(value);
        if(CastType.BYTE.equals(type)) return CastType.BYTE.handler.apply(value);
        if(CastType.INTEGER_PRIMITIVE.equals(type)) return CastType.INTEGER_PRIMITIVE.handler.apply(value);
        if(CastType.BOOLEAN_PRIMITIVE.equals(type)) return CastType.BOOLEAN_PRIMITIVE.handler.apply(value);
        if(CastType.FLOAT_PRIMITIVE.equals(type)) return CastType.FLOAT_PRIMITIVE.handler.apply(value);
        if(CastType.DOUBLE_PRIMITIVE.equals(type)) return CastType.DOUBLE_PRIMITIVE.handler.apply(value);
        if(CastType.LONG_PRIMITIVE.equals(type)) return CastType.LONG_PRIMITIVE.handler.apply(value);
        if(CastType.SHORT_PRIMITIVE.equals(type)) return CastType.SHORT_PRIMITIVE.handler.apply(value);
        if(CastType.BYTE_PRIMITIVE.equals(type)) return CastType.BYTE_PRIMITIVE.handler.apply(value);
        throw new Exception(
                "type %s is not valid. try one of them : '%s'".formatted(
                    type.getTypeName(), 
                    Stream.of(CastType.values()).map(CastType::getType).toString()
                )
            );
    }

    private enum CastType{

        STRING( "java.lang.String", obj -> obj ),
        INTEGER("java.lang.Integer",Integer::valueOf),
        BOOLEAN("java.lang.Boolean",Boolean::valueOf),
        FLOAT(  "java.lang.Float",  Float::valueOf),
        DOUBLE( "java.lang.Double", Double::valueOf),
        LONG(   "java.lang.Long",   Long::valueOf),
        SHORT(  "java.lang.Short",  Short::valueOf),
        BYTE(   "java.lang.Byte",   Byte::valueOf),

        INTEGER_PRIMITIVE("int",    Integer::parseInt),
        BOOLEAN_PRIMITIVE("boolean",Boolean::parseBoolean),
        FLOAT_PRIMITIVE(  "float",  Float::parseFloat),
        DOUBLE_PRIMITIVE( "double", Double::parseDouble),
        LONG_PRIMITIVE(   "long",   Long::parseLong),
        SHORT_PRIMITIVE(  "short",  Short::parseShort),
        BYTE_PRIMITIVE(   "byte",   Byte::parseByte),

        ;

        private final String type;
        private final Function<String,Object> handler;

        private CastType(String type, Function<String, Object> handler) {
            this.type = type;
            this.handler = handler;
        }

        public String getType() {
            return type;
        }


        public boolean equals(Class<?> type){
            return this.type.equals(type.getTypeName());
        }

        
    }


    public static String normalizeNameEnvVar(String nameVar) {
        if (nameVar == null)
            return "";

        return Normalizer.normalize(nameVar, Normalizer.Form.NFKD)
                .replaceAll("\\p{M}", "")
                .trim()
                .replace(" ", "_")
                .replace(".", "_")
                .replace("-", "_")
                .toUpperCase();
    }

    public static String handleDeclarationsInValues(Map<String, String> mapVars, String value) {
        return handleDeclarationsInValues(mapVars, value, null);
    }

    private static String handleDeclarationsInValues(Map<String, String> mapVars, String valueObject, String nameVarToAvoidRecursion) {
        if (valueObject == null) return null;

        nameVarToAvoidRecursion = normalizeNameEnvVar(nameVarToAvoidRecursion);

        Pattern r = Pattern.compile("\\$\\{.*?}");
        Matcher m = r.matcher(valueObject);
        while (m.find()) {
            String keyReplacementGroup = m.group();
            String keyReplacementNameVar = normalizeNameEnvVar(m.group().replaceAll("^\\$\\{", "").replaceAll("}$", ""));

            if (keyReplacementNameVar.trim().equals(""))
                throw new IllegalArgumentException("name var between ${} cannot be empty");

            String defaultValue = "";

            if (keyReplacementNameVar.contains(":") && !keyReplacementNameVar.endsWith(":")) {
                String[] arrayKeyReplacementWithDefaultValue = keyReplacementNameVar.split(":");
                keyReplacementNameVar = arrayKeyReplacementWithDefaultValue[0];
                defaultValue = arrayKeyReplacementWithDefaultValue[1];
            }

            if (keyReplacementNameVar.equals(nameVarToAvoidRecursion))
                throw new IllegalArgumentException(
                        String.format("var %s has a value that requires itself", nameVarToAvoidRecursion));

            String valueFind =  String.valueOf(System.getenv(keyReplacementNameVar));

            if (valueFind.equals("null")) {
                valueFind = String.valueOf(mapVars.get(keyReplacementNameVar));
                if (!valueFind.equals("null")) {
                    valueFind = (String) handleDeclarationsInValues(mapVars, valueFind, keyReplacementNameVar);
                }
            }

            if (valueFind.equals("null")) {
                valueFind = defaultValue;
            }
            valueObject = valueObject.replace(keyReplacementGroup, valueFind);
            valueObject = valueObject.trim().replaceAll("^\"", "").replaceAll("\"$", "");

        }
        return valueObject;
    }

    public static void handleDeclarationsInMap(final Map<String, String> mapVars) {
        Map<String, String> mapVarWithNormalizedKey = mapVars
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> normalizeNameEnvVar(entry.getKey()),
                        Map.Entry::getValue));
        mapVars.clear();
        mapVars.putAll(mapVarWithNormalizedKey);
        mapVars.entrySet().forEach(
                entry -> entry.setValue(handleDeclarationsInValues(mapVars, entry.getValue(), entry.getKey())));
    }

    public static Collector<String, List<String>, Map<String, String>> collectVarsToMap(final String delimiter,
            final String prefixVar) {

        Objects.requireNonNull(delimiter);
        Objects.requireNonNull(prefixVar);

        String patterString = String.format("^%s.*%s.*", prefixVar, delimiter);

        return new Collector<String, List<String>, Map<String, String>>() {
            @Override
            public Supplier<List<String>> supplier() {
                return ArrayList::new;
            }

            @Override
            public BiConsumer<List<String>, String> accumulator() {
                return (list, stringItem) -> {
                    if (stringItem != null && stringItem.matches(patterString)) {
                        String stringItemWithNoPrefix = Objects.equals(prefixVar, "")
                                ? stringItem
                                : stringItem.replaceFirst(prefixVar, "");

                        String[] keyAndValueArray = stringItemWithNoPrefix.split(delimiter);

                        String stringItemNormalized = String.format("%s%s%s",
                                normalizeNameEnvVar(keyAndValueArray[0]),
                                delimiter,
                                keyAndValueArray[1] != null
                                        ? keyAndValueArray[1].trim().replaceAll("^\"", "").replaceAll("\"$", "")
                                        : "");
                        list.add(stringItemNormalized);
                    }
                };
            }

            @Override
            public BinaryOperator<List<String>> combiner() {
                return (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                };
            }

            @Override
            public Function<List<String>, Map<String, String>> finisher() {
                return list -> list.stream()
                        .collect(Collectors.toMap(s -> s.split(delimiter)[0], s -> s.split(delimiter)[1]));
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Collections.singleton(Characteristics.UNORDERED);
            }
        };
    }

}
