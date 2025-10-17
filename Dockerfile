# Sử dụng base image chính thức của Eclipse Temurin (Java 21)
FROM eclipse-temurin:21-jdk-jammy AS build

# Đặt thư mục làm việc là BTL (nơi có pom.xml)
WORKDIR /app/BTL

# Sao chép file pom.xml và build các dependency
COPY BTL/pom.xml .
RUN mvn dependency:go-offline

# Sao chép toàn bộ code và thực hiện build Spring Boot
COPY BTL/src src
RUN mvn package -DskipTests

# — Giai đoạn RUNTIME —
# Sử dụng base image nhẹ hơn để chạy (Java JRE)
FROM eclipse-temurin:21-jre-jammy

# Đặt biến môi trường cho port
ENV PORT 8080

# Sao chép file JAR từ giai đoạn build
# Lưu ý: Thay BTL-0.0.1-SNAPSHOT.jar bằng tên JAR chính xác của bạn
COPY --from=build /app/BTL/target/BTL-0.0.1-SNAPSHOT.jar app.jar

# Lệnh khởi động ứng dụng
CMD ["java", "-jar", "/app.jar"]