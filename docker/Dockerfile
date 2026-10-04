FROM eclipse-temurin:25-jre

WORKDIR /app

COPY webgoat-src/target/webgoat-2026.4-SNAPSHOT.jar /app/webgoat.jar

EXPOSE 8080 9090

ENTRYPOINT ["java", "-jar", "/app/webgoat.jar", "--server.address=0.0.0.0"]
