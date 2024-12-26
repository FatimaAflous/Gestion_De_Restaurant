# Étape 1 : Utiliser une image de base OpenJDK
FROM openjdk:17-jdk

# Étape 2 : Définir un répertoire de travail
VOLUME /tmp

# Étape 3 : Copier le fichier JAR généré dans l'image
COPY target/*.jar app.jar

# Étape 4 : Spécifier la commande pour démarrer l'application
ENTRYPOINT ["java", "-jar", "/app.jar"]
