FROM openjdk:17-alpine

ARG JAR_FILE_PATH=build/libs/*-1.0-SNAPSHOT.jar
COPY ${JAR_FILE_PATH} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
