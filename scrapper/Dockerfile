FROM openjdk:17.0.1-jdk-slim

ARG JAR_FILE=target/*.jar

WORKDIR /opt/app

COPY ${JAR_FILE} scrapper.jar

ENTRYPOINT ["java","-jar","scrapper.jar"]