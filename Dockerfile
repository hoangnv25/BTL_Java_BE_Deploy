# SỬ DỤNG IMAGE ĐÃ CÓ MAVEN
FROM maven:3.9.5-eclipse-temurin-21 AS build

# Đặt thư mục làm việc là /app/BTL
WORKDIR /app/BTL

# Sao chép file pom.xml để tải dependency trước
COPY BTL/pom.xml .
RUN mvn dependency:go-offline

# Sao chép toàn bộ mã nguồn (phải có ./)
COPY BTL/src ./src

# Build ứng dụng, bỏ qua test
RUN mvn package -DskipTests

# — Giai đoạn RUNTIME —
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Biến môi trường
ENV PORT 8080

# Sao chép file JAR từ giai đoạn build
COPY --from=build /app/BTL/target/BTL-0.0.1-SNAPSHOT.jar app.jar

# Chạy app
CMD ["java", "-jar", "app.jar"]
