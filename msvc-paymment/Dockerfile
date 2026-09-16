# Etapa 1: Compilar el código fuente con Maven y JDK 21
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final ligera con JRE 21
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Al usar puerto 0, Spring Boot asigna un puerto aleatorio internamente,
# por lo que no es estrictamente necesario exponer un puerto fijo aquí.
ENTRYPOINT ["java", "-jar", "app.jar"]