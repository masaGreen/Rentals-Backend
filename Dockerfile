# Use Maven and OpenJDK 20 as the base image for the build stage
FROM maven:3.8.6-openjdk-20 AS build

# Set the working directory in the container
WORKDIR /app

# Copy only the necessary files for building the application
COPY pom.xml .
COPY src ./src

# Build the application with Maven and skip tests
RUN mvn clean package -DskipTests

# Use a lightweight base image for the final deployment
FROM openjdk:20-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file built from the previous stage
COPY --from=build /app/target/Rental-Units-Management-0.0.1-SNAPSHOT.jar RentalUnitsManagement.jar

# Expose the application port
EXPOSE 8080

# Specify the command to run your application
ENTRYPOINT ["java", "-jar", "RentalUnitsManagement.jar"]
