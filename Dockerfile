# FROM amazoncorretto:21-alpine-jdk

# COPY target/springboot-crud-0.0.1-SNAPSHOT.jar app.jar

# ENTRYPOINT [ "java", "-jar", "/app.jar" ]

FROM maven:3.9.6-amazoncorretto-21-alpine AS build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]