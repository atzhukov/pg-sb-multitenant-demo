FROM eclipse-temurin:21

RUN groupadd -r spring && useradd -r -g spring spring

USER spring:spring

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
