# Stage 1: Build
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
WORKDIR /build

# Copiem întâi pom.xml pentru a profita de layer caching pe dependențe
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiem codul sursă și generăm JAR-ul
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Creăm un utilizator non-root (least privilege)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copiem doar artefactul compilat din stage-ul anterior
COPY --from=builder /build/target/*.jar app.jar
RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080

# Exec-form pentru ca JVM-ul să primească direct SIGTERM la docker stop
ENTRYPOINT ["java", "-jar", "app.jar"]