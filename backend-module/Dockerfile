FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml ./
COPY backend-module/pom.xml backend-module/pom.xml
COPY ejb-module/pom.xml ejb-module/pom.xml
COPY backend-module/src backend-module/src
COPY ejb-module/src ejb-module/src

RUN mvn -B -pl backend-module -am -DskipTests clean package && cp backend-module/target/*.jar app.jar

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/app.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
