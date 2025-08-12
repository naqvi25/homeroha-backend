# FROM jelastic/maven:3.9.5-openjdk-21 AS build
# COPY . .
# RUN mvn clean package -DskipTests
#
# FROM openjdk:21-jdk-slim
# COPY --from=build /target/homeroha-0.0.1-SNAPSHOT.jar homio.jar
# EXPOSE 8080
# ENTRYPOINT ["java","-jar","homio.jar"]