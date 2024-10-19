FROM openjdk:17-jdk-slim

# Install Tesseract
RUN apt-get update && apt-get install -y tesseract-ocr tesseract-ocr-eng

# Set working directory
WORKDIR /app

# Add the Spring Boot application jar
COPY target/ocr-worker.jar /app/app.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
