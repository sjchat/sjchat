FROM openjdk:8-alpine

RUN mkdir app
WORKDIR /app
COPY target/messages-0.0.1-SNAPSHOT-jar-with-dependencies.jar /app

EXPOSE 50052

ENTRYPOINT ["java", "-jar", "messages-0.0.1-SNAPSHOT-jar-with-dependencies.jar"]