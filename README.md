# Classdoor Backend

Bem-vindo ao repositório do backend do projeto **Classdoor**, construído com **Spring Boot 3**, **Java 21**, **PostgreSQL** e documentado via **Springdoc OpenAPI (Swagger)**.

---

## 🚀 Guia de Execução Local (Para Desenvolvedores e Membros da Equipe)

Este tutorial orienta como configurar e rodar o backend localmente utilizando o banco de dados PostgreSQL via **Docker Compose**.

### 1. Pré-requisitos
Certifique-se de ter instalado em sua máquina:
- **Java Development Kit (JDK) 21**
- **Maven** (versão 3.8+)
- **Docker** e **Docker Compose**
- *(Opcional)* **Cloudflared** (apenas se for utilizar a conexão com a VPS)

---

### 2. Inicialização do Banco de Dados Local (Docker Compose)

O projeto inclui um arquivo `docker-compose.yml` pré-configurado na raiz para subir uma instância local do **PostgreSQL 16**.

1. **Subir o container do PostgreSQL em segundo plano:**
   ```bash
   docker compose up -d
   ```
2. **Verificar se o container está ativo e saudável:**
   ```bash
   docker compose ps
   ```
3. **Comandos úteis para o Docker Compose:**
   - Visualizar os logs do banco em tempo real:
     ```bash
     docker compose logs -f postgres
     ```
   - Parar o banco de dados mantendo os dados persistidos:
     ```bash
     docker compose down
     ```
   - Reiniciar o container:
     ```bash
     docker compose restart
     ```

> 💾 **Persistência de Dados:** Os dados do banco são armazenados em um volume gerenciado pelo Docker (`classdoor_postgres_data`), persistindo mesmo após parar o container.

---

### 3. Configuração do `.env` para o Banco Local

O Spring Boot lê automaticamente as variáveis de ambiente a partir do arquivo `.env` na raiz do projeto.

1. Crie o arquivo `.env` na raiz do repositório copiando o modelo de exemplo:
   ```bash
   cp .env.example .env
   ```
2. Certifique-se de que o `.env` contenha as credenciais do banco local configuradas no Docker Compose:
   ```env
   SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/classdoor_db
   SPRING_DATASOURCE_USERNAME=postgres
   SPRING_DATASOURCE_PASSWORD=postgres123
   SERVER_PORT=8080
   ```
   > 💡 **Nota:** O Flyway aplicará automaticamente as migrações na inicialização da aplicação e o `DataInitializer` criará os dados iniciais de teste (professores, cursos e turmas).

3. Para rodar o backend localmente via Maven:
   ```bash
   mvn spring-boot:run
   ```
4. Com a aplicação rodando, acesse a documentação interativa do **Swagger UI** no navegador:
   `http://localhost:8080/swagger-ui/index.html`

---

### 4. (Opcional) Conexão com o Banco Remoto de Desenvolvimento (VPS)

Caso precise se conectar ao banco centralizado hospedado na VPS via Cloudflare Tunnel em vez do banco local:

1. Execute o túnel TCP:
   ```bash
   cloudflared access tcp --hostname db.odominoapp.win --url 127.0.0.1:5432
   ```
2. No seu arquivo `.env`, altere o nome do banco e credenciais:
   ```env
   SPRING_DATASOURCE_URL=jdbc:postgresql://127.0.0.1:5432/classdoor_dev_db
   SPRING_DATASOURCE_USERNAME=<<SOLICITAR_AO_CTO_GABRIEL>>
   SPRING_DATASOURCE_PASSWORD=<<SOLICITAR_AO_CTO_GABRIEL>>
   SERVER_PORT=8080
   ```

---

### 5. Acesso aos Logs do Sistema (Grafana Loki)
Para visualizar os logs dos serviços rodando em produção ou desenvolvimento na VPS (ideal para QA e desenvolvedores sem acesso SSH), disponibilizamos um painel centralizado em Grafana:

- **URL de Acesso:** [https://logs.odominoapp.win](https://logs.odominoapp.win)
- **Usuário:** `<<SOLICITAR_AO_CTO_GABRIEL>>`
- **Senha:** `<<SOLICITAR_AO_CTO_GABRIEL>>`
  *(Solicite as credenciais de acesso ao painel de logs diretamente ao CTO Gabriel-Aragao).*

No painel do Grafana (Explore -> Loki), você pode filtrar os logs por container, unit do systemd (`classdoor-prod`, `classdoor-dev`) ou por nível de erro em tempo real.

---

## ⚠️ AVISOS DE SEGURANÇA E BOAS PRÁTICAS

1. **Configurações Locais Proibidas no Git:** 
   Arquivos como `.env`, `.env.local`, logs locais ou credenciais de banco de dados locais **jamais** devem ser enviados para o GitHub. 
2. **Uso do `.gitignore`:** 
   O projeto já possui um `.gitignore` configurado para ignorar arquivos de configuração sensíveis e de ambiente. Sempre verifique o comando `git status` antes de realizar um `git add` ou `git commit`.
