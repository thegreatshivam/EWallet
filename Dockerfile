# Step 1: Build the application
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# 1. Copy ONLY the pom.xml (the list of libraries)
COPY pom.xml .

# 2. Download libraries. This is the "heavy" part that will now be CACHED.
# If you don't change pom.xml, Docker will skip this next time!
RUN mvn dependency:go-offline -B

# 3. Copy your source code (the parts you edit)
COPY src ./src

# 4. Build the jar (Fast because libraries are already there)
RUN mvn clean package -DskipTests

# Step 2: Run the application
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]