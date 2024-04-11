FROM maven:3.8.4-amazoncorretto-17 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn clean install

FROM amazoncorretto:17.0.7-alpine

WORKDIR /app

COPY --from=build /app/target .

RUN java -jar llm_wx-0.0.1-SNAPSHOT.jar

EXPOSE 8081