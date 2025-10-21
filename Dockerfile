# Stage 1: Build ứng dụng bằng Maven
FROM maven:3.9.5-eclipse-temurin-21 AS build

WORKDIR /app

# Sao chép toàn bộ code (bao gồm BTL/pom.xml và BTL/src)
COPY . .

# Di chuyển vào thư mục chứa pom.xml
WORKDIR /app/BTL

# Build ứng dụng (bỏ qua test)
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy file JAR từ stage build
COPY --from=build /app/BTL/target/BTL-0.0.1-SNAPSHOT.jar app.jar

# Chạy app
CMD ["java", "-jar", "app.jar"]
