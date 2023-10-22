FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/Order-Microservice-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} Order-Microservice-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/Order-Microservice-0.0.1-SNAPSHOT.jar"]

