FROM eclipse-temurin:21

WORKDIR /app

COPY target/com.employee.security-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

