FROM openjdk:17.0.1-jdk-slim

ARG JAR_FILE=target/*.jar

WORKDIR /opt/app

COPY ${JAR_FILE} bot.jar

ENTRYPOINT ["java","-jar","bot.jar"]