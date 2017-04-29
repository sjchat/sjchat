FROM maven:3.5-jdk-8-alpine

# From https://keyholesoftware.com/2015/01/05/caching-for-maven-docker-builds/
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

# selectively add the POM file
ADD pom.xml /usr/src/app/

# get all the downloads out of the way
RUN mvn verify clean --fail-never

ADD . /usr/src/app
RUN mvn verify

EXPOSE 8080

ENV MESSAGE_SERVICE_HOST message-service
ENV USER_SERVICE_HOST user-service

CMD ["mvn", "exec:java"]