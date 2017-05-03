FROM openjdk:8-alpine

RUN mkdir app
WORKDIR /app
COPY target/restapi-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

ENV MESSAGE_SERVICE_HOST message-service
ENV USER_SERVICE_HOST user-service

ENTRYPOINT ["java", "-jar", "restapi-0.0.1-SNAPSHOT.jar"]