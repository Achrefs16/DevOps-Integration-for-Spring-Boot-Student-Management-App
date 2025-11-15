# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copier le fichier pom.xml pour télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Compiler et créer le JAR
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Créer un utilisateur non-root pour la sécurité
RUN addgroup -S spring && adduser -S spring -G spring

# Copier le JAR depuis le stage de build
COPY --from=build /app/target/*.jar app.jar

# Changer le propriétaire
RUN chown -R spring:spring /app

# Utiliser l'utilisateur non-root
USER spring:spring

# Exposer le port
EXPOSE 7000

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:7000/actuator/health || exit 1

# Variables d'environnement par défaut
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Lancer l'application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
