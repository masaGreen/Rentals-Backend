FROM eclipse-temurin:17-jdk-alpine

LABEL maintainer="davidmachariamj@gmail.com"

# Copy the JAR file built from the previous stage
COPY target/Rental-Units-Management-0.0.1-SNAPSHOT.jar RentalUnitsManagement.jar

# Expose the application port
EXPOSE 8080

# Specify the command to run your application
ENTRYPOINT ["java", "-jar", "RentalUnitsManagement.jar"]
