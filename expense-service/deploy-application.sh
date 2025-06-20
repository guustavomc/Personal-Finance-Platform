#!/bin/bash

set -e  # Exit on error

# Load environment variables from secret.env
if [ -f "secret.env" ]; then
  echo "🔐 Loading environment variables from secret.env..."
  export $(grep -v '^#' secret.env | xargs)
else
  echo "❌ secret.env file not found!"
  exit 1
fi

# Start PostgreSQL container
echo "🐳 Starting PostgreSQL container..."
docker-compose -f db-docker-compose.yml up -d

# Optional: Wait for PostgreSQL to be ready
echo "⏳ Waiting for PostgreSQL to be ready..."
until docker exec expense-service-db pg_isready -U "$POSTGRES_USER" > /dev/null 2>&1; do
  sleep 1
done
echo "✅ PostgreSQL is ready."

# Run Spring Boot application
echo "🚀 Starting Spring Boot application..."
./mvnw spring-boot:run

# Optional: Stop Docker container when app exits (uncomment below to enable)
# echo "🛑 Stopping Docker container..."
# docker-compose down
