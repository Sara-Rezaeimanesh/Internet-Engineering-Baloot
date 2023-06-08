# Use a Maven image as the base
FROM maven:3.8.1-openjdk-11 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml file to the container
COPY pom.xml .

# Download the project dependencies
RUN mvn dependency:go-offline

# Copy the source code to the container
COPY src ./src

# Build the project
RUN mvn package -DskipTests

# Use an OpenJDK image as the base for the final image
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the built artifact from the previous stage to the container
COPY --from=build /app/target/Balootv5-0.0.1-SNAPSHOT.jar ./app.jar

# Set the command to run the application
CMD ["java", "-jar", "app.jar"]
