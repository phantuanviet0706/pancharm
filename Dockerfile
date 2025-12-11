# Stage 1: Build
# Use Maven with Amazon Corretto JDK 24
FROM maven:3.9.11-amazoncorretto-24 AS build

# Copy source code and pom.xml to /app folder
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build source with maven
RUN mvn package -DskipTests

# Stage 2: Create image
# Start with Amazon Corretto JDK 24
FROM amazoncorretto:24

# Set working folder to App and copy compiled file from above step
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

COPY src/main/resources /app/resources

EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]