# Use Maven for the build stage to compile the project
FROM maven:3.8.7-eclipse-temurin-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies first (helps with caching)
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy the complete project source into the container
COPY src ./src

# Compile and package the application as a JAR file
RUN mvn clean package -DskipTests

# Use a lightweight JDK image for the runtime stage
FROM eclipse-temurin:17-jdk-jammy

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage to the runtime stage
COPY --from=build /app/target/mu-server-app-0.0.1-SNAPSHOT.jar mu-server-app-0.0.1-SNAPSHOT.jar

# Expose the port your Mu server runs on
EXPOSE 8086

# Run the application
ENTRYPOINT ["java", "-jar", "mu-server-app-0.0.1-SNAPSHOT.jar"]
