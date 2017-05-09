FROM openjdk:8-alpine

RUN mkdir app
WORKDIR /app
COPY target/users-0.0.1-SNAPSHOT-jar-with-dependencies.jar /app

EXPOSE 50051

ENV DATABASE_HOST database

ENTRYPOINT ["java", "-jar", "users-0.0.1-SNAPSHOT-jar-with-dependencies.jar"]
