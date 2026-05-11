#!/bin/sh

echo "======================="
echo "SPRING BOOT DEPLOYMENT"
echo "======================="

cd "$(dirname "$0")"
echo "kill"
netstat -ano | findstr :8083
echo "Building project..."
./mvn clean install

echo "Starting application..."
./mvn spring-boot:run