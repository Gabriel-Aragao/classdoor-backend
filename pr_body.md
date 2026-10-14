## 🎯 Objetivo
Implementação do Card B005: Sistema robusto de Autenticação e Gestão de Usuários com Spring Security 6, PostgreSQL e Tokens JWT (HS256).

## 📋 Entregas Realizadas
- **Modelagem & Entidades:** `User` com papéis `ROLE_STUDENT` e `ROLE_PROFESSOR`, `Role` enum e `UserRepository`.
- **Segurança & JWT:** `JwtService` (geração e validação de tokens JWT), `JwtAuthenticationFilter` (filtro stateless por requisição) e `SecurityConfig` (configuração de rotas públicas/protegidas e codificação BCrypt).
- **DTOs & Controllers:** Endpoints RESTful em `/api/auth/register`, `/api/auth/login` e `/api/auth/me` com validação Jakarta Bean Validation.
- **Testes Unitários:** `AuthServiceTest` cobrindo fluxo de registro com sucesso.
- **Build & CI:** Compilação e testes validados com sucesso (Java 21 + Spring Boot 3.3.4).
