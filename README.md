# NavFlowCash - Backend API (Spring Boot 3)

Backend robusto desenvolvido em **Java 17/21** com **Spring Boot 3**, **Spring Data JPA**, **PostgreSQL**, **Validation** e **Swagger / OpenAPI**.

## 🚀 Como Executar

### Pré-requisitos
- Java 17 ou superior
- Maven 3.8+
- PostgreSQL rodando localmente ou no Docker

### 1. Criar o banco de dados PostgreSQL
```sql
CREATE DATABASE flowcash_db;
```

### 2. Configurar o `application.properties`
Ajuste as credenciais de banco e o token do Mercado Pago em `src/main/resources/application.properties`.

### 3. Rodar a aplicação
```bash
mvn clean spring-boot:run
```

A API estará rodando em: `http://localhost:8080`
A documentação Swagger estará em: `http://localhost:8080/swagger-ui.html`

## 📦 Estrutura de Pacotes (`br.com.navdata.flowcash`)
- `entity/`: Entidades JPA (User, Account, Category, Transaction, ClosedMonth, Payment, SystemConfig)
- `dto/`: Records imutáveis com validações Bean Validation
- `repository/`: Interfaces Spring Data JPA
- `service/`: Regras de negócio, cálculos financeiros, parcelamento e conciliação
- `controller/`: Endpoints REST padronizados
- `exception/`: Tratamento global de erros (RFC 7807 ProblemDetail)
- `config/`: CORS e Swagger
