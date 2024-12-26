# Étape 1 : Utiliser une image Java comme base
FROM openjdk:17-jdk-alpine

# Étape 2 : Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Étape 3 : Copier le JAR dans l'image Docker
COPY target/mon-application.jar app.jar

# Étape 4 : Spécifier la commande pour démarrer l'application
CMD ["java", "-jar", "app.jar"]
