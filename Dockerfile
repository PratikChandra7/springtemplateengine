FROM openjdk:17-jdk-alpine
MAINTAINER Niveus Solutions
COPY target/springtemplateengine-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]