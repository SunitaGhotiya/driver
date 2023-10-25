FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/uberDriver-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} uberDriver-app.jar
ENTRYPOINT ["java","-jar","/uberDriver-app.jar"]