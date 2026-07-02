# Build stage
FROM maven:3.8.4-eclipse-temurin-17 AS build
WORKDIR /app
# Copier les fichiers de configuration Maven
COPY pom.xml .
RUN mvn dependency:go-offline
# Copier le code source
COPY src ./src
# Builder l'application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
# Copier le JAR depuis le stage de build
COPY --from=build /app/target/*.jar app.jar
# Exposer le port
EXPOSE 8083
# Démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]