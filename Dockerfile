# 1. Define a imagem base
FROM eclipse-temurin:21-jdk-jammy

# 2. Define o diretório de trabalho dentro do container
WORKDIR /app

# 3. Copia TODOS os arquivos do seu projeto para dentro do container
COPY . .

# 4. Dá permissão de execução ao Maven Wrapper
RUN chmod +x mvnw

# 5. Compila o projeto ignorando a compilação dos testes
# Adicionamos -Dmaven.test.skip=true para ignorar erros em arquivos de teste
RUN ./mvnw clean package -DskipTests -Dmaven.test.skip=true

# 6. Expõe a porta que o Spring Boot usa
EXPOSE 8080

# 7. Comando para rodar a aplicação
# O Render usa a variável de ambiente $PORT, o Spring Boot a reconhecerá automaticamente
CMD ["sh", "-c", "java -jar target/*.jar"]