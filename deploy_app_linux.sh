#!/bin/bash

# Build the rest-api jar file
if cd ./ocr_worker; then
    if mvn clean package -DskipTests; then
        echo "Maven build successful."
    else
        echo "Maven build failed!"
        exit 1
    fi
else
    echo "Directory ./ocr_worker not found!"
    exit 1
fi