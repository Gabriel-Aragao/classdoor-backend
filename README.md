# Classdoor Backend

Bem-vindo ao repositório do backend do projeto **Classdoor**, construído com **Spring Boot 3**, **Java 21**, **PostgreSQL** e documentado via **Springdoc OpenAPI (Swagger)**.

---

## 🚀 Guia de Execução Local (Para Desenvolvedores e Membros da Equipe)

Este tutorial passo a passo orienta como configurar e rodar o backend conectando-se ao banco de dados de desenvolvimento centralizado na VPS.

### 1. Pré-requisitos
Certifique-se de ter instalado em sua máquina:
- **Java Development Kit (JDK) 21**
- **Maven** (versão 3.8+)
- **Node.js** (versão 18+) e npm/yarn (para rodar o frontend)
- **Cloudflared** (necessário para o túnel do banco de dados)

---

### 2. Conexão com o Banco de Dados de Desenvolvimento (VPS)
Em vez de rodar um PostgreSQL localmente, o time de desenvolvimento se conecta diretamente ao banco de dados `classdoor_dev_db` hospedado na VPS através do Cloudflare Tunnel.

1. **Ative o túnel TCP na sua máquina local:**
   Abra o seu terminal local e execute o comando do Cloudflare para abrir a porta `5432` localmente:
   ```bash
   cloudflared access tcp --hostname db.odominoapp.win --url 127.0.0.1:5432
   ```
2. Mantenha esse terminal aberto enquanto estiver desenvolvendo.

---

### 3. Configuração do Backend Local (`classdoor-backend`)

Como o Spring Boot suporta nativamente arquivos `.env` na raiz do projeto, você não precisa criar arquivos de propriedades locais.

1. Clone o repositório e navegue até a pasta do projeto.
2. Crie um arquivo `.env` na **raiz** do repositório `classdoor-backend` com as seguintes variáveis de ambiente locais:
   ```env
   SPRING_DATASOURCE_URL=jdbc:postgresql://127.0.0.1:5432/classdoor_dev_db
   SPRING_DATASOURCE_USERNAME=classdoor_dev_user
   SPRING_DATASOURCE_PASSWORD=pass
   SERVER_PORT=8080
   ```
   > 💡 **Nota:** O arquivo `.env` é ignorado pelo Git e **NUNCA** deve ser commitado ou enviado para o repositório remoto.

3. Para rodar o backend localmente via Maven:
   ```bash
   mvn spring-boot:run
   ```
4. Com a aplicação rodando, acesse a documentação interativa do **Swagger UI** no navegador:
   `http://localhost:8080/swagger-ui/index.html`

---

### 4. Acesso aos Logs do Sistema (Grafana Loki)
Para visualizar os logs dos serviços rodando em produção ou desenvolvimento na VPS (ideal para QA e desenvolvedores sem acesso SSH), disponibilizamos um painel centralizado em Grafana:

- **URL de Acesso:** [https://logs.odominoapp.win](https://logs.odominoapp.win)
- **Usuário:** `admin`
- **Senha:** `classdoor_secure_2026`

No painel do Grafana (Explore -> Loki), você pode filtrar os logs por container, unit do systemd (`classdoor-prod`, `classdoor-dev`) ou por nível de erro em tempo real.

---

## ⚠️ AVISOS DE SEGURANÇA E BOAS PRÁTICAS

1. **Configurações Locais Proibidas no Git:** 
   Arquivos como `.env`, `.env.local`, logs locais ou credenciais de banco de dados locais **jamais** devem ser enviados para o GitHub. 
2. **Uso do `.gitignore`:** 
   O projeto já possui um `.gitignore` configurado para ignorar arquivos de configuração sensíveis e de ambiente. Sempre verifique o comando `git status` antes de realizar um `git add` ou `git commit`.
