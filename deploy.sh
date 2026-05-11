#!/bin/sh

echo "======================="
echo "SPRING BOOT DEPLOYMENT"
echo "======================="

cd "$(dirname "$0")"

echo "Building project..."
./mvnw clean install

echo "Starting application..."
./mvnw spring-boot:run