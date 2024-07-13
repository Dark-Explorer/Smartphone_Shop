FROM eclipse-temurin:22-jdk-alpine
VOLUME /tmp
COPY target/smartphoneshop-0.0.1-SNAPSHOT.jar app.jar
LABEL authors="diema"

ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 2004