FROM eclipse-temurin:22-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
LABEL authors="diema"

ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 2004