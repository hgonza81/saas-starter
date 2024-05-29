FROM ubuntu:22.04

RUN apt-get update && apt-get install -y openjdk-17-jdk

VOLUME /tmp
ADD /target/saas-bootstrap-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]