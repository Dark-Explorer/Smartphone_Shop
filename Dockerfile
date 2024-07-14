FROM eclipse-temurin:22-jdk-alpine
VOLUME /tmp
COPY target/smartphoneshop-0.0.1-SNAPSHOT.jar smartphoneshop-0.0.1-SNAPSHOT.jar
LABEL authors="diema"

ENTRYPOINT ["java", "-jar", "/smartphoneshop-0.0.1-SNAPSHOT.jar"]
EXPOSE 2004

#FROM openjdk:22
#COPY --from=build target/smartphoneshop-0.0.1-SNAPSHOT.jar smartphoneshop-0.0.1-SNAPSHOT.jar
#EXPOSE 2004
#ENTRYPOINT ["java", "-jar", "/smartphoneshop-0.0.1-SNAPSHOT.jar"]