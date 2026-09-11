# Etapa 1: Compilar el código fuente con Maven y JDK 25
FROM maven:3.9-eclipse-temurin-25-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Crear una imagen final ligera usando el JRE 25
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# El comando de arranque buscará las variables que definiste en el compose
ENTRYPOINT ["java", "-jar", "app.jar"]
