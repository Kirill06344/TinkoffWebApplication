FROM openjdk:17.0.1-jdk-slim

ARG JAR_FILE=target/bot-1.0-SNAPSHOT.jar

WORKDIR /opt/app

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]