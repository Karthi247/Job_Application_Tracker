FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml ./
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
RUN useradd --system --create-home --uid 10001 appuser
COPY --from=build --chown=appuser:appuser /workspace/target/Job_Application_Tracker-0.0.1-SNAPSHOT.war /app/app.war
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.war"]
