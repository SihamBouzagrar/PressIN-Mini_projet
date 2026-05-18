#!/bin/sh

echo "======================="
echo "SPRING BOOT DEPLOYMENT"
echo "======================="

# Se positionner dans le dossier où se trouve le script
cd "$(dirname "$0")"

echo "Arrêt du processus sur le port 8083..."
# Récupère le PID du processus sur le port 8083 et le tue
PID=$(netstat -ano | findstr :8083 | awk '{print $5}' | head -n 1)

if [ ! -z "$PID" ]; then
    echo "Killing process $PID..."
    taskkill /F /PID $PID
else
    echo "Aucun processus trouvé sur le port 8083."
fi

echo "Building project..."
# Utilise 'mvn' directement (ou './mvnw' si présent)
mvn clean install -DskipTests

echo "Starting application..."
mvn spring-boot:run
