# MICROSERVICES JAKARTA EE 10 WITH EMBEDDED SERVER

This is a project to show how to create microservices on jakarta ee 10 with embeded server using:
  - `jersey` as embeded server rest container
  - `hibernate + hikaricp` as datasource connection pooling
  - `activemq-client-jakarta` as queue mensaging
  - `log4j-slf4j2` as logging management

## CORE PROJECTS

  - **[core/activemq](./core/activemq)**
  - **[core/config](./core/config)**
  - **[core/datasource](./core/datasource)**
  - **[core/log](./core/log)**
  - **[core/rest](./core/rest)**


## MICROSERVICES

  - **[microservices/ms-company](./microservices/ms-company)**

## RUNNING MICROSERVICES IN CONTAINERS

We have all container configurations in [container](./container) folder

### RUNNING MICROSERVICES IN CONTAINER
```bash
# build all modules
./gradlew clean build -x test

# start containers
docker-compose  --file ./container/docker-compose.yml up -d

```

### RUNING CODE QUALITY ANALYSIS

### RUNNING ONLY UNIT TEST
```bash
# running  unit test in all modules
./gradlew test

# running  unit test in specific module
./gradlew :ms-company:test

```

### Running code covering Analysis 
```bash
# running analysis in all modules
./gradlew jacocoTestReport

# running  analysis in specific module
./gradlew :ms-company:jacocoTestReport

```
> We will see the output in `./build/reports/jacoco/test/html/index.html` from each project

### Running code quality Analysis in sonarqube
```bash
# start sonar container in http://localhost:9000
docker-compose --file ./container/sonar.docker-compose.yml up -d

# running analysis in all modules
./gradlew sonar

# running  analysis in specific module
./gradlew :ms-company:sonar

```
> Don't forget to set you sonar token in [gradle.properties](./gradle.properties)