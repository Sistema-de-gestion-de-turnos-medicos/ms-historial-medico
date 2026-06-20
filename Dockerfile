# ============================================
# Etapa 1: Construcción (build) con Maven + JDK 21
# ============================================
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Se copia primero el pom.xml para aprovechar la cache de dependencias de Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Se copia el resto del código fuente y se genera el .jar (sin correr los tests)
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============================================
# Etapa 2: Imagen final, liviana, solo con el JRE
# ============================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Usuario no-root por buenas prácticas de seguridad
RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=build /app/target/*.jar app.jar

RUN chown spring:spring app.jar
USER spring

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]
