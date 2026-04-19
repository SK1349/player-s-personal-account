# ===== ЭТАП 1: Сборка =====
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Кэшируем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Копируем исходники
COPY src ./src

# Собираем JAR
RUN mvn clean package -DskipTests -B

# ===== ЭТАП 2: Запуск =====
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 🔥 ВАЖНО: проверьте имя JAR в target/ после сборки!
# Обычно: artifactId-version.jar (смотрите в pom.xml)
COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 8084
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=docker"]