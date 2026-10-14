# Classdoor Backend

Bem-vindo ao repositório do backend do projeto **Classdoor**, construído com **Spring Boot 3**, **Java 21**, **PostgreSQL** e documentado via **Springdoc OpenAPI (Swagger)**.

---

## 🚀 Guia de Execução Local (Para Desenvolvedores e Membros da Equipe)

Este tutorial passo a passo orienta como configurar e rodar o backend e o frontend simultaneamente no seu computador de desenvolvimento local.

### 1. Pré-requisitos
Certifique-se de ter instalado em sua máquina:
- **Java Development Kit (JDK) 21**
- **Maven** (versão 3.8+)
- **PostgreSQL** (versão 16+)
- **Node.js** (versão 18+) e npm/yarn (para rodar o frontend)

---

### 2. Configuração do Banco de Dados Local
1. Abra o seu cliente PostgreSQL (DBeaver, psql, pgAdmin) e crie um banco de dados local para desenvolvimento:
   ```sql
   CREATE DATABASE classdoor_dev;
   ```
2. Opcionalmente, crie um usuário local ou utilize o seu superusuário do Postgres.

---

### 3. Configuração do Backend Local (`classdoor-backend`)

1. Clone o repositório e navegue até a pasta do projeto.
2. Crie ou edite o arquivo de configuração local em `src/main/resources/application-local.yml` (ou configure as variáveis de ambiente equivalentes):
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/classdoor_dev
       username: seu_usuario_postgres
       password: sua_senha_postgres
     jpa:
       hibernate:
         ddl-auto: update
       show-sql: true

   server:
     port: 8080
   ```
   > 💡 **Nota:** O arquivo `application-local.yml` **NUNCA** deve ser commitado ou enviado para o repositório remoto, pois ele contém credenciais específicas da sua máquina.

3. Para rodar o backend localmente via Maven:
   ```bash
   mvn spring-boot:run
   ```
4. Com a aplicação rodando, acesse a documentação interativa do **Swagger UI** no navegador:
   `http://localhost:8080/swagger-ui/index.html`

---

### 4. Configuração do Frontend Local (`classdoor-frontend`)

1. Navegue até o repositório do frontend.
2. Instale as dependências:
   ```bash
   npm install
   ```
3. Crie um arquivo `.env.local` na raiz do frontend apontando para o seu backend local:
   ```env
   NEXT_PUBLIC_API_URL=http://localhost:8080
   ```
4. Inicie o servidor de desenvolvimento do frontend:
   ```bash
   npm run dev
   ```
5. Acesse a aplicação no navegador em `http://localhost:3000`.

---

## ⚠️ AVISOS DE SEGURANÇA E BOAS PRÁTICAS

1. **Configurações Locais Proibidas no Git:** 
   Arquivos como `application-local.yml`, `.env.local`, logs locais ou credenciais de banco de dados locais **jamais** devem ser enviados para o GitHub. 
2. **Uso do `.gitignore`:** 
   O projeto já possui um `.gitignore` configurado para ignorar arquivos de configuração sensíveis e de ambiente. Sempre verifique o comando `git status` antes de realizar um `git add` ou `git commit` para garantir que apenas código-fonte e arquivos de template (`application.yml.example`) sejam enviados.
