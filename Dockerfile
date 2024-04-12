FROM maven:3.9.2-amazoncorretto-17 AS build

WORKDIR /app

# COPY pom.xml .
COPY . .

RUN mvn clean install

FROM amazoncorretto:17.0.7-alpine

WORKDIR /app

COPY --from=build /app/target .

EXPOSE 8081

CMD ["java", "-jar", "llm_wx-0.0.1-SNAPSHOT.jar"]
