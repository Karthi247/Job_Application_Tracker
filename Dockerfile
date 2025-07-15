FROM eclipse-temurin:21-jdk
WORKDIR /app1
COPY target/Job_Application_Tracker-0.0.1-SNAPSHOT.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Job_Application_Tracker-0.0.1-SNAPSHOT.war"]