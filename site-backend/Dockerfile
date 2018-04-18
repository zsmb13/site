FROM openjdk:8-jdk-alpine

VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} site.jar
ENTRYPOINT ["java","-Dspring.data.mongodb.host=mongo:27017","-Djava.security.egd=file:/dev/./urandom","-jar","site.jar"]
