#BACKEND

# Paso 1: Usar una imagen de Maven para compilar el proyecto
# FROM maven:4.0.0-openjdk-17 AS build
FROM maven:3.9.9-amazoncorretto-17-alpine AS build

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo pom.xml y las dependencias primero (para usar el cache de Docker)
COPY pom.xml .

# Descargar las dependencias (esto se cachea en Docker para acelerar futuras construcciones)
RUN mvn dependency:go-offline -B

# Copiar todo el código fuente de la aplicación
COPY src ./src

# Compilar y empaquetar la aplicación
RUN mvn clean package -DskipTests

# Paso 2: Usar una imagen más liviana de OpenJDK para ejecutar la aplicación
FROM openjdk:17-jdk-slim

# Crear un directorio de trabajo en la imagen final
WORKDIR /app

# Copiar el archivo JAR generado en el paso de construcción
COPY --from=build /app/target/SimuladorPagos-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]