FROM eclipse-temurin:17-jdk-alpine as build

WORKDIR app

RUN apk update && apk add gcompat binutils

RUN jlink \
    --module-path /opt/java/openjdk/jmods \
    --add-modules java.base,java.logging,java.desktop,java.management,java.naming,java.security.jgss,java.prefs,jdk.httpserver,java.sql,java.xml,java.security.sasl,java.instrument \
    --output jre-custom \
    --strip-debug \
    --no-header-files \
    --no-man-pages \
    --compress 2

# reduce a bit more docker size (-4MB)
RUN strip -p --strip-unneeded jre-custom/lib/server/libjvm.so && \
   find jre-custom -name '*.so' | xargs -i strip -p --strip-unneeded {}

FROM alpine:latest

ARG PORT_APP
ARG PROJECT_PATH
ARG JVM_ARGS

WORKDIR /deployment

COPY --from=build /app/jre-custom jre-custom/
COPY ${PROJECT_PATH}/build/libs/*.jar app.jar


ENV ENV_JVM_ARGS ${JVM_ARGS}
ENV ENV_PORT_APP ${PORT_APP}

CMD jre-custom/bin/java ${ENV_JVM_ARGS} -jar app.jar
# CMD ["sleep","infinity"]

EXPOSE ${ENV_PORT_APP}

