#!/bin/bash

# Build the rest-api jar file
cd ./rest_api
if mvn clean package -DskipTests; then
    echo "Maven build successful."
else
    echo "Maven build failed!"
    exit 1
fi

# Build the docker image of the rest-api
if docker build -t springboot-backend-app .; then
    echo "Docker image built successfully."
else
    echo "Docker image build failed!"
    exit 1
fi

cd ../

# Run all containers
if docker-compose up -d; then
    echo "All containers are up and running."
else
    echo "Failed to start containers with Docker Compose!"
    exit 1
fi
