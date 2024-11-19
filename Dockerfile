# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-alpine

# Copy the JAR file to the container
ADD target/gatewayservice-0.0.1-SNAPSHOT.jar gatewayservice-0.0.1-SNAPSHOT.jar

# Run the application
ENTRYPOINT [ "java","-jar","gatewayservice-0.0.1-SNAPSHOT.jar" ]
