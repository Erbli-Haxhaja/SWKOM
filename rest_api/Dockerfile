# Use the official OpenJDK image as a base image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file into the container (update 'your-app.jar' to match your jar name)
COPY target/rest-api.jar /app/app.jar

# Expose port 8081 to be accessible outside the container
EXPOSE 8081

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
