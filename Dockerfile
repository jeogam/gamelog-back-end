# 1. Etapa de Construção (Build)
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY src .
# Dá permissão de execução ao Maven Wrapper
RUN chmod +x mvnw
# Compila o projeto e gera o .jar (pula os testes para ser mais rápido no deploy)
RUN ./mvnw clean package -DskipTests

# 2. Etapa de Execução (Run)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copia apenas o .jar gerado na etapa anterior
COPY --from=build /app/target/*.jar app.jar
# Expõe a porta padrão do Spring Boot
EXPOSE 8080
# Comando para iniciar o servidor
ENTRYPOINT ["java", "-jar", "app.jar"]