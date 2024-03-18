FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN mvn install -DskipTests

FROM openjdk:17-jdk-alpine
COPY --from=build /app/target/bbqms-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
