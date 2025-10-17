# SỬ DỤNG IMAGE ĐÃ CÓ MAVEN
# Image chính thức của Maven đã bao gồm JDK
FROM maven:3.9.5-eclipse-temurin-21 AS build

# Đặt thư mục làm việc là BTL (nơi có pom.xml)
WORKDIR /app/BTL

# Sao chép file pom.xml và build các dependency
# Cấu trúc project của bạn là BTL/pom.xml, nên đường dẫn COPY phải là BTL/pom.xml
COPY BTL/pom.xml .
RUN mvn dependency:go-offline

# Sao chép toàn bộ code và thực hiện build Spring Boot
# Cấu trúc project của bạn là BTL/src, nên đường dẫn COPY phải là BTL/src
COPY BTL/src src
RUN mvn package -DskipTests

# — Giai đoạn RUNTIME —
# Sử dụng base image nhẹ hơn để chạy (Java JRE)
FROM eclipse-temurin:21-jre-jammy

# Đặt biến môi trường cho port
ENV PORT 8080

# Sao chép file JAR từ giai đoạn build
# Kiểm tra lại tên file JAR chính xác
COPY --from=build /app/BTL/target/BTL-0.0.1-SNAPSHOT.jar app.jar

# Lệnh khởi động ứng dụng
CMD ["java", "-jar", "/app.jar"]