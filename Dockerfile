# ... (suas linhas anteriores de FROM e WORKDIR)

# Copia TODO o projeto para dentro do container
COPY . .

# Agora sim o mvnw estará lá para receber permissão
RUN chmod +x mvnw

# Compila o projeto
RUN ./mvnw clean package -DskipTests

# ... (restante do seu Dockerfile)