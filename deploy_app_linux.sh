#!/bin/bash

# Build the rest-api jar file
if cd ./rest_api; then
    if mvn clean package -DskipTests; then
        echo "Maven build successful."
    else
        echo "Maven build failed!"
        exit 1
    fi
else
    echo "Directory ./rest_api not found!"
    exit 1
fi