# Etapa 1: Compilar el jar usando Maven y JDK 25
FROM maven:3.9-eclipse-temurin-25-alpine AS builder
WORKDIR /app

# Copiar el archivo de configuración de Maven y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el código fuente y compilar el archivo JAR omitiendo pruebas
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Crear la imagen final ligera de ejecución con JRE 25
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Copiar el JAR generado en la etapa anterior
COPY --from=builder /app/target/*.jar eureka-server.jar

# Exponer el puerto oficial de Eureka
EXPOSE 8761

# Comando para ejecutar el servidor Eureka
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]