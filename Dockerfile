# Image légère avec Java
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copier le JAR déjà buildé par Maven
COPY target/*.jar app.jar

# Exposer le port
EXPOSE 8089

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]