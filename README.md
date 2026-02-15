# 💳 High-Concurrency E-Wallet API

A robust Spring Boot 3 RESTful service designed to manage e-wallet balances. This application is engineered to handle high-load environments (up to 1000 requests per second per wallet) while maintaining strict data integrity and consistency using Pessimistic Locking.

---

## 🛠 Tech Stack
* **Java 17**
* **Spring Boot 3.x** (Web, Data JPA, Validation)
* **PostgreSQL 15**
* **Liquibase** (Database migrations)
* **Docker & Docker Compose**
* **JUnit 5 & MockMvc** (Integration testing)

---

## ⚖️ Concurrency & Performance
The project is specifically optimized for the requirement of **1000 RPS per wallet**:

* **Pessimistic Locking**: Implemented using `@Lock(LockModeType.PESSIMISTIC_WRITE)` at the repository level. This ensures that when a transaction is modifying a wallet balance, other concurrent requests must wait for the lock to release, preventing "Lost Updates."
* **Transaction Management**: All operations are wrapped in `@Transactional` to ensure atomicity. If a withdrawal fails (e.g., insufficient funds), the system ensures no partial data is written.
* **Database Connection Pooling**: Optimized via HikariCP to manage high-frequency requests and prevent 50X errors during peak load.

---

## 🚀 Getting Started

### Prerequisites
* Docker and Docker Compose installed.
* *Note: The build happens within the Docker container; local Maven/Java is not required.*

### Running the Application
1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd <project-folder>
   ```

2. **Launch the system**:
   ```bash
   docker-compose up --build
   ```
   This command:
    - **Builds** the JAR using an optimized multi-stage Dockerfile (caching dependencies).
    - **Starts** the PostgreSQL 15 database.
    - **Executes** Liquibase migrations to create the schema and seed a test wallet.
    - **Exposes** the API on `http://localhost:8080`.

---

## ⚙️ Configuration
The system supports configuration via environment variables without requiring a container rebuild. Default values are provided using `${VARIABLE:-default}` syntax in `docker-compose.yml`:

| Variable | Description | Default |
| :--- | :--- | :--- |
| `DB_NAME` | Database name | `wallet_db` |
| `DB_USER` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `secret` |

---

## 📖 API Documentation

### 1. Wallet Operation (Deposit/Withdraw)
**POST** `/api/v1/wallet`

**Request Body:**
```json
{
  "valletId": "550e8400-e29b-41d4-a716-446655440000",
  "operationType": "DEPOSIT",
  "amount": 1000
}
```
*Valid `operationType` values: `DEPOSIT`, `WITHDRAW`.*

### 2. Get Balance
**GET** `/api/v1/wallets/{WALLET_UUID}`

**Response Body:**
`2000.00`

---

## 🧪 Testing & Validation
Endpoints are covered by integration tests using **MockMvc**.

**Scenarios Tested:**
- ✅ **Success Paths**: Valid deposits and balance retrieval.
- ✅ **Insufficient Funds**: Returns `400 Bad Request` with a descriptive message.
- ✅ **Invalid JSON**: Handled by Global Exception Handler (returns `400`).
- ✅ **Non-existent Wallet**: Returns `404 Not Found`.

---

## 📁 Project Structure
- `src/main/java/.../controller`: REST API endpoints.
- `src/main/java/.../service`: Business logic and locking mechanisms.
- `src/main/java/.../model`: Data Transfer Objects with `@Valid` constraints.
- `src/main/resources/db/changelog`: Liquibase migration scripts.
- `Dockerfile`: Optimized multi-stage build.