# Этап 1 — сборка проекта
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Копируем pom.xml и зависимости (для кэширования)
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем весь исходный код и собираем проект
COPY src ./src
RUN mvn clean package -DskipTests

# Этап 2 — запуск приложения
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Копируем jar из предыдущего этапа
COPY --from=builder /app/target/*.jar app.jar

# Запускаем
ENTRYPOINT ["java", "-jar", "app.jar"]
