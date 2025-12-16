# 1. Define a imagem base (Java 17 é o padrão comum, ajuste se usar 21 ou outro)
FROM eclipse-temurin:17-jdk-jammy

# 2. Define o diretório de trabalho dentro do container
WORKDIR /app

# 3. Copia TODOS os arquivos do seu projeto para dentro do container
COPY . .

# 4. Dá permissão de execução ao Maven Wrapper
RUN chmod +x mvnw

# 5. Compila o projeto e gera o arquivo .jar
RUN ./mvnw clean package -DskipTests

# 6. Expõe a porta que o Spring Boot usa (padrão é 8080)
EXPOSE 8080

# 7. Comando para rodar a aplicação
# ATENÇÃO: O nome do arquivo .jar dentro de target/ pode variar.
# O *.jar pega qualquer arquivo jar gerado lá dentro.
CMD ["sh", "-c", "java -jar target/*.jar"]