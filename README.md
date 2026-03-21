# Personal-Finance-Platform

A RESTful API platform for managing personal finances, built with Spring Boot. This project enables users to create and manage monthly/yearly budgets, track expenses, and handle investments through a microservices architecture. The platform is designed to be scalable, testable, and extensible, with JWT-based authentication and authorization handled by a dedicated Auth Service.

## Project Overview

The Personal-Finance-Platform consists of four microservices:

- **Auth Service**: Handles user registration and login, issuing JWT tokens used to authenticate requests across the platform.
- **Budget Service**: Responsible for creating and managing budgets for specific months or years. It orchestrates interactions with the Expense and Investment Services.
- **Expense Service**: Manages expense records, including creating, retrieving, updating, and deleting expenses. This service is called directly by the Budget Service.
- **Investment Service**: Handles investment tracking and management, also called directly by the Budget Service.

## Tech Stack

- **Framework**: Spring Boot
- **Language**: Java
- **Database**: Configurable via Spring Data JPA (currently uses an in-memory database for testing, configurable in `application.properties`)
- **Build Tool**: Maven
- **API Design**: RESTful
- **Validation**: Spring Validation
- **Future Plans**: Spring Security, Docker, API Gateway, Service Discovery

# Detailed Description of Each Service

## Expense Service

The Expense Service is a fully implemented microservice responsible for managing expense records. It provides endpoints for creating, retrieving, updating, and deleting expenses, as well as filtering expenses by category or month.

### Features

- Create new expenses with details such as category, date, description, and amount.
- Retrieve all expenses or filter by ID, category, or month/year.
- Update existing expenses.
- Delete expenses by ID.
- Returns structured JSON responses with appropriate HTTP status codes.

### Project Structure

```
src/
 └── main/
 │   ├── java/com/example/expense
 │   │   ├── ExpenseServiceApplication.java        # Main application entry point
 │   │   │
 │   │   ├── controller/
 │   │   │   └── ExpenseController.java            # REST controller for expense endpoints
 │   │   │   └── ExpenseReportController.java      # REST controller for expense report endpoints
 │   │   ├── dto/
 │   │   │   ├── CreateExpenseRequest.java         # DTO for creating/updating expenses
 │   │   │   └── ExpenseResponse.java              # DTO for expense response data
 │   │   │   └── ExpenseSummaryResponse.java       # DTO for expense summary response data
 │   │   ├── exception/
 │   │   │   ├── ExpenseNotFoundException.java     # Custom Exception
 │   │   │   └── GlobalExceptionHandler.java       # Handle responses for exception
 │   │   ├── model/
 │   │   │   └── Expense.java                      # Entity model for expenses
 │   │   │   └── PaymentMethod.java                # Enum for payment method
 │   │   ├── repository/
 │   │   │   └── ExpenseRepository.java            # JPA repository for expense persistence
 │   │   ├── service/
 │   │   │   └── ExpenseService.java               # Business logic for expense operations
 │   │   │   └── ExpenseReportService.java         # Business logic for expense report operations
 │   └── resources/
 │       ├── application.properties                # Configuration file
 │       └── ...
k8s/
 └───── postgres-deployment.yaml
 │  └── postgres-service.yaml
 │  └── db-secrets.yaml
 │  └── expense-deployment.yaml
 │  └── expense-service.yaml
 │
 db-docker-compose.yaml
 Dockerfile
```
### API Endpoints

| Method | Endpoint                       | Description                                        | Request Body                  | Response Body             |
|--------|--------------------------------|----------------------------------------------------|-------------------------------|---------------------------|
| GET    | `/api/expense`                 | Retrieve all expenses                              | None                          | List of `ExpenseResponse` |
| GET    | `/api/expense/{id}`            | Retrieve expense by ID                             | None                          | List of `ExpenseResponse` |
| GET    | `/api/expense/category`        | Retrieve expenses by category                      | Query param: `category`       | List of `ExpenseResponse` |
| POST   | `/api/expense`                 | Create a new expense                               | `CreateExpenseRequest`        | List of `ExpenseResponse` |
| PUT    | `/api/expense/{id}`            | Update an existing expense                         | `CreateExpenseRequest`        | List of `ExpenseResponse` |
| DELETE | `/api/expense/{id}`            | Delete an expense by ID                            | None                          | None (204 No Content)     |
| GET    | `/api/expense/summary/monthly` | Retrieve  Summarized expenses for a specific month | Query params: `year`, `month` | `ExpenseSummaryResponse`  |
| GET    | `/api/expense/summary/annual`  | Retrieve  Summarized expenses for a specific year  | Query params: `year`          | `ExpenseSummaryResponse`  |
| GET    | `/api/expense/report/monthly`  | Retrieve  List of expenses for a specific month    | Query params: `year`, `month` | List of `ExpenseResponse` |
| GET    | `/api/expense/report/annual`   | Retrieve  List of expenses for a specific year     | Query params: `year`          | List of `ExpenseResponse` |
| GET    | `/api/expense/total/monthly`   | Retrieve  Amount Spent from specific month         | Query params: `year`, `month` | BigDecimal                |
| GET    | `/api/expense/total/annual`   | Retrieve  Amount Spent from specific year          | Query params: `year`          | BigDecimal |

### Example Request/Response

**Create Expense (POST `/api/expense`)**

Request:
```json
{
  "category": "Food",
  "date": "2025-06-18",
  "description": "Grocery shopping",
  "valueSpent": 50.00
}
```

Response:
```json
{
  "id": 1,
  "category": "Food",
  "date": "2025-06-18",
  "description": "Grocery shopping",
  "valueSpent": 50.00
}
```


### Getting Started

#### Prerequisites

- Java 17 or higher
- Maven 3.8+
- IDE (e.g., IntelliJ IDEA, Eclipse)
- Database (configurable via `application.properties`; uses PostgreSQL)

### Configuration

Edit `src/main/resources/application.properties` to configure the database or other settings. Configuration made for Postgres:

```properties
# PostgreSQL DB Configuration
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update

# Optional: Logging
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Kubernetes cluster deployment

1. Create or start Kind Cluster
   ```bash
   kind create cluster --name <cluster-name>
   ```

2. Create Kubernetes Secret for DB credentials:
   ```bash
   kubectl apply -f k8s/db-secrets.yaml
   ```

3. Deploy PostgreSQL to the cluster:
   ```bash
    kubectl apply -f k8s/postgres-deployment.yaml
    kubectl apply -f k8s/postgres-service.yaml
   ```
   
- Optional: check status
  ```bash
  kubectl get pods
  kubectl get svc
  ```

4. Build the application:
   ```bash
   mvn clean package
   ```

5. Load the Docker Image:
   - For this project we load the local expense-api image into Kind:

    ```bash
    docker build -t expense-api .
    kind load docker-image expense-api:latest --name <cluster-name>
    ```
   - Make sure the image: field in expense-deployment.yaml matches.
   

   - **Important**: If you were getting dockerhub image, replace <your-dockerhub-username>/expense-api:latest with your Docker Hub username in the image field. 
   
   - If you used the local image with kind load docker-image, use image: expense-api:latest instead.

6. Apply the Manifests:
   ```bash
   kubectl apply -f k8s/expense-deployment.yaml
   kubectl apply -f k8s/expense-service.yaml
   ```

7. Expose via NodePort (or Ingress):
   - Your expense-service.yaml exposes the app on port 30080:

   ```bash
   kubectl get svc expense-api-service
   ```
   - You can now access the app at:http://<node-ip>:30080
   

8. Verify the Deployment:
   ```bash
    kubectl get deployments
    kubectl get pods
    kubectl get services
   ```

9. For Kind or other local clusters, check the node’s IP:
   ```bash
   kubectl get nodes -o wide
   ```
   - Use the INTERNAL-IP of a node.


10. Port Forwarding:
   ```bash
   kubectl port-forward service/expense-api-service 8080:80
   ```
- Access the API at http://localhost:8080/api/expense.


## Investment Service

The Investment Service manages investment records, including creating, updating, and retrieving investment details, with integration into the overall budgeting process.

### Project Structure

```
src/
 └── main/
 │   ├── java/com/example/investment
 │   │   ├── InvestmentServiceApplication.java        # Main application entry point
 │   │   │
 │   │   ├── controller/
 │   │   │   └── InvestmentController.java            # REST controller for investment endpoints
 │   │   │   └── PortfolioSummaryController.java      # REST controller for portfolio endpoints
 │   │   ├── dto/
 │   │   │   ├── CreateInvestmentRequest.java         # DTO for creating/updating investment
 │   │   │   └── InvestmentResponse.java              # DTO for investment response data
 │   │   │   └── PortfolioSummaryResponse.java        # DTO for portfolio summary response data
 │   │   ├── exception/
 │   │   │   ├── InvestmentNotFoundException.java     # Custom Exception
 │   │   │   └── GlobalExceptionHandler.java          # Handle responses for exception
 │   │   ├── model/
 │   │   │   └── Investment.java                      # Entity model for investment
 │   │   │   └── InvestmentType.java                  # Enum for investment type
 │   │   │   └── AssetHolding.java                    # Entity model for current assets
 │   │   ├── repository/
 │   │   │   └── InvestmentRepository.java            # JPA repository for investment persistence
 │   │   ├── service/
 │   │   │   └── InvestmentService.java               # Business logic for investment operations
 │   │   │   └── PortfolioSummaryService.java         # Business logic for portfolio operations
 │   └── resources/
 │       ├── application.properties                   # Configuration file
 │       └── ...
k8s/
 └───── postgres-deployment.yaml
 │  └── postgres-service.yaml
 │  └── db-secrets.yaml
 │  └── investment-deployment.yaml
 │  └── investment-service.yaml
 │
 db-docker-compose.yaml
 Dockerfile
```

### API Endpoints

| Method | Endpoint                           | Description                    | Request Body               | Response Body               |
|--------|------------------------------------|--------------------------------|----------------------------|-----------------------------|
| GET    | `/api/investment/invest`                  | Retrieve all investments       | None                       | List of `InvestmentResponse` |
| GET    | `/api/investment/invest/{id}`             | Retrieve investment by ID      | Query param: `id`          | `InvestmentResponse`    |
| GET    | `/api/investment/invest/type`             | Retrieve investments by type   | Query param: `investmentType`  | List of `InvestmentResponse`   |
| POST   | `/api/investment/invest`                  | Create a new investment        | `CreateInvestmentRequest ` | `InvestmentResponse`    |
| PUT    | `/api/investment/invest/{id}`             | Update an existing investment  | `CreateInvestmentRequest `<br/>Query param: `id`  | `InvestmentResponse`   |
| DELETE | `/api/investment/invest/{id}`             | Delete an investment by ID     | None                       | None (204 No Content)       |
| GET    | `/api/investment/withdrawal`       | Retrieve all withdrawals       | None                       | List of `WithdrawalResponse` |
| GET    | `/api/investment/withdrawal/{id}`  | Retrieve withdrawals by ID     | Query param: `id`          | `WithdrawalResponse`    |
| GET    | `/api/investment/withdrawal/type`  | Retrieve withdrawals by type   | Query param: `investmentType`  | List of `WithdrawalResponse`   |
| POST   | `/api/investment/withdrawal`       | Create a new withdrawal        | `CreateWithdrawalRequest ` | `WithdrawalResponse`    |
| PUT    | `/api/investment/withdrawal/{id}`  | Update an existing withdrawal  | `CreateWithdrawalRequest`<br/>Query param: `id` | `WithdrawalResponse`   |
| DELETE | `/api/investment/withdrawal/{id}`  | Delete an withdrawal by ID     | None                       | None (204 No Content)       |
| GET    | `api/investment/portfolio/summary` | Retrieve  Investment Portfolio | None                       | `PortfolioSummaryResponse`    |

### Example Request/Response

**Create Investment (POST `/api/investment/invest`)**

Request:
```json
{
   "investmentType": "STOCK",
   "assetSymbol": "GOOGL",
   "amountInvested": 1500.00,
   "quantity": 4.0,
   "investmentDate": "2025-08-10",
   "currency": "USD"
}
```

Response:
```json
{
   "id": 2,
   "investmentType": "STOCK",
   "assetSymbol": "GOOGL",
   "amountInvested": 1500.00,
   "quantity": 4.0,
   "investmentDate": "2025-08-10",
   "currency": "USD",
   "alternateAmount": 0.0,
   "alternateCurrency": ""
}
```
### Getting Started

#### Prerequisites

- Java 17 or higher
- Maven 3.8+
- IDE (e.g., IntelliJ IDEA, Eclipse)
- Database (configurable via `application.properties`; uses PostgreSQL)

### Configuration

Edit `src/main/resources/application.properties` to configure the database or other settings. Configuration made for Postgres:

```properties
# PostgreSQL DB Configuration
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update

# Optional: Logging
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```


### Kubernetes cluster deployment

1. Create or start Kind Cluster
   ```bash
   kind create cluster --name <cluster-name>
   ```

2. Create Kubernetes Secret for DB credentials:
   ```bash
   kubectl apply -f k8s/db-secrets.yaml
   ```

3. Deploy PostgreSQL to the cluster:
   ```bash
    kubectl apply -f k8s/postgres-deployment.yaml
    kubectl apply -f k8s/postgres-service.yaml
   ```

- Optional: check status
  ```bash
  kubectl get pods
  kubectl get svc
  ```

4. Build the application:
   ```bash
   mvn clean package
   ```

5. Load the Docker Image:
   - For this project we load the local investment-api image into Kind:

    ```bash
    docker build -t investment-api .
    kind load docker-image investment-api:latest --name <cluster-name>
    ```
   - Make sure the image: field in investment-deployment.yaml matches.


- **Important**: If you were getting dockerhub image, replace <your-dockerhub-username>/investment-api:latest with your Docker Hub username in the image field.
  
- If you used the local image with kind load docker-image, use image: expense-api:latest instead.

6. Apply the Manifests:
   ```bash
   kubectl apply -f k8s/investment-deployment.yaml
   kubectl apply -f k8s/investment-service.yaml
   ```

7. Expose via NodePort (or Ingress):
   - Your investment-service.yaml exposes the app on port 30080:

   ```bash
   kubectl get svc investment-api-service
   ```
   - You can now access the app at:http://<node-ip>:30080


8. Verify the Deployment:
   ```bash
    kubectl get deployments
    kubectl get pods
    kubectl get services
   ```

9. For Kind or other local clusters, check the node’s IP:
   ```bash
   kubectl get nodes -o wide
   ```
   - Use the INTERNAL-IP of a node.

10. Port Forwarding:
   ```bash
   kubectl port-forward service/investment-api-service 8080:80
   ```

- Access the API at http://localhost:8080/api/investment.


### Budget Service

The Budget Service allows users to define budgets for specific months or years, integrating with the Expense and Investment Services to track spending and investment allocations.

### Project Structure

```
src/
 └── main/
 │   ├── java/com/budget
 │   │   ├── BudgetServiceApplication.java          # Main application entry point
 │   │   │
 │   │   ├── configuration/
 │   │   │   └── AuditingConfig.java
 │   │   ├── controller/
 │   │   │   └── BudgetController.java              # REST controller for investment endpoints
 │   │   ├── dto/
 │   │   │   ├── BudgetCategoryRequest.java         # DTO for creating/updating budget category
 │   │   │   ├── BudgetItem.java                    # DTO for creating/updating budget item
 │   │   │   ├── BudgetResponse.java                # DTO for returning Budget information
 │   │   │   ├── CreateBudgetRequest.java           # DTO for creating Budget
 │   │   │   ├── ExpenseResponse.java               # DTO for creating Expense data
 │   │   │   └── InvestmentResponse.java            # DTO for creating Investment data
 │   │   ├── exception/
 │   │   │   ├── BudgetNotFoundException.java       # Custom Exception
 │   │   │   └── GlobalExceptionHandler.java        # Handle responses for exception
 │   │   ├── model/
 │   │   │   ├── Budget.java                        # Entity model for Budget
 │   │   │   ├── BudgetCategory.java                # Model for category for budgets
 │   │   │   ├── BudgetPeriodType.java              # Model for setting different time periods
 │   │   │   ├── BudgetStatus.java                  # Enum for setting different status
 │   │   │   └── CategoryType.java                  # Enum for Expense or Investment category type
 │   │   ├── repository/
 │   │   │   └── BudgetRepository.java              # JPA repository for budget persistence
 │   │   ├── service/
 │   │   │   ├── BudgetItemService.java             # Business logic for budget item creation
 │   │   │   ├── BudgetService.java                 # Business logic for Budget operations
 │   │   │   ├── ExpenseServiceClient.java          # Business logic for consuming Expense info
 │   │   │   └── InvestmentServiceClient.java       # Business logic for consuming Investment info
 │   └── resources/
 │       ├── application.properties                 # Configuration file
 │       └── ...
k8s/
 └───── postgres-deployment.yaml
 │  └── postgres-service.yaml
 │  └── db-secrets.yaml
 │  └── budget-deployment.yaml
 │  └── budget-service.yaml
 │
 db-docker-compose.yaml
 Dockerfile
```

### API Endpoints

| Method | Endpoint                   | Description                   | Request Body               | Response Body               |
|--------|----------------------------|-------------------------------|----------------------------|-----------------------------|
| GET    | `/api/budget/{id}`         | Retrieve Budget by ID         | Query param: `id`          | `BudgetResponse`    |
| GET    | `/api/budget/{id}/refresh` | Retrieve Updated Budget by ID | Query param: `id`           | `BudgetResponse`     |
| POST   | `/api/budget`              | Create a new Budget           | `CreateBudgetRequest ` | `BudgetResponse`    |
| DELETE | `/api/budget/{id}`         | Delete Budget by ID           | Query param: `id` | None (204 No Content)|
| PUT | `/api/budget/{id}`         | Update Budget by ID           | `CreateBudgetRequest `Query param: `id` | `BudgetResponse`|


### Example Request/Response

**Create Budget (POST `/api/budget`)**

Request:
```json
{
  "name": "Budget Test",
  "budgetPeriodType": "MONTHLY",
  "startDate": "2026-04-01",
  "totalPlannedAmount": 3000.00,
  "categories": [
    {
      "categoryName": "Rent",
      "plannedAmount": 1200.00,
      "type": "EXPENSE"
    }
  ]
}
```

Response:
```json
{
  "budgetPeriodType": "MONTHLY",
  "budgetStatus": "ACTIVE",
  "categories": [
    {
      "actualInvested": 0,
      "actualSpent": 0,
      "categoryName": "Rent",
      "id": 24,
      "lastSynced": null,
      "percentageOfTotal": 0,
      "plannedAmount": 1200.00,
      "type": "EXPENSE"
    }
  ],
  "createdAt": null,
  "endDate": "2026-04-30",
  "id": 9,
  "name": "Budget Test",
  "startDate": "2026-04-01",
  "totalActualInvested": 0,
  "totalActualSpent": 0,
  "totalPlannedAmount": 3000.00,
  "updatedAt": null,
  "userId": null
}
```
### Getting Started

#### Prerequisites

- Java 17 or higher
- Maven 3.8+
- IDE (e.g., IntelliJ IDEA, Eclipse)
- Database (configurable via `application.properties`; uses PostgreSQL)

### Configuration

Edit `src/main/resources/application.properties` to configure the database or other settings. Configuration made for Postgres:

```properties
# PostgreSQL DB Configuration
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update

# Optional: Logging
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

```


### Kubernetes cluster deployment

1. Create or start Kind Cluster
   ```bash
   kind create cluster --name <cluster-name>
   ```

2. Create Kubernetes Secret for DB credentials:
   ```bash
   kubectl apply -f k8s/db-secrets.yaml
   ```

3. Deploy PostgreSQL to the cluster:
   ```bash
    kubectl apply -f k8s/postgres-deployment.yaml
    kubectl apply -f k8s/postgres-service.yaml
   ```

- Optional: check status
  ```bash
  kubectl get pods
  kubectl get svc
  ```

4. Build the application:
   ```bash
   mvn clean package
   ```

5. Load the Docker Image:
    - For this project we load the local investment-api image into Kind:

    ```bash
    docker build -t budget-api .
    kind load docker-image budget-api:latest --name <cluster-name>
    ```
    - Make sure the image: field in investment-deployment.yaml matches.


- **Important**: If you were getting dockerhub image, replace:
  - <your-dockerhub-username>/budget-api:latest with your Docker Hub username in the image field.

- If you used the local image with kind load docker-image, use image: 
  - budget-api:latest instead.

6. Apply the Manifests:
   ```bash
   kubectl apply -f k8s/budget-deployment.yaml
   kubectl apply -f k8s/budget-service.yaml
   ```

7. Expose via NodePort (or Ingress):
    - Your budget-service.yaml exposes the app on port 30080:

   ```bash
   kubectl get svc budget-api-service
   ```
    - You can now access the app at:http://<node-ip>:30080


8. Verify the Deployment:
   ```bash
    kubectl get deployments
    kubectl get pods
    kubectl get services
   ```

9. For Kind or other local clusters, check the node’s IP:

   - Use the INTERNAL-IP of a node.
   ```bash
   kubectl get nodes -o wide
   ```
10. Port Forwarding:
   ```bash
   kubectl port-forward service/budget-api-service 8080:80
   ```

   - Access the API at http://localhost:8080/api/budget.

## Auth Service

The Auth Service is responsible for user registration and authentication. It issues signed JWT tokens upon successful login or registration, which must be included in the `Authorization` header of requests to protected endpoints across the platform.

### Features

- Register a new user with username, email, and password.
- Authenticate an existing user and receive a signed JWT token.
- Passwords are hashed using BCrypt — plain-text passwords are never stored.
- Token validation is handled via a per-request filter, keeping other services stateless.
- Role-based access control support with `USER` and `ADMIN` roles.

### Project Structure

```
src/
 └── main/
 │   ├── java/com/auth
 │   │   ├── AuthServiceApplication.java          # Main application entry point
 │   │   │
 │   │   ├── config/
 │   │   │   └── SecurityConfig.java              # Spring Security and filter chain configuration
 │   │   ├── controller/
 │   │   │   └── AuthController.java              # REST controller for auth endpoints
 │   │   ├── dto/
 │   │   │   ├── RegisterRequest.java             # DTO for registration input
 │   │   │   ├── LoginRequest.java                # DTO for login input
 │   │   │   └── AuthResponse.java                # DTO for token response
 │   │   ├── entity/
 │   │   │   ├── User.java                        # Entity model for users
 │   │   │   └── Role.java                        # Enum for user roles (USER, ADMIN)
 │   │   ├── repository/
 │   │   │   └── UserRepository.java              # JPA repository for user persistence
 │   │   ├── security/
 │   │   │   ├── JwtFilter.java                   # Per-request JWT validation filter
 │   │   │   └── JwtUtil.java                     # JWT generation and parsing utilities
 │   │   ├── service/
 │   │   │   └── AuthService.java                 # Business logic for register and login
 │   └── resources/
 │       ├── application.properties               # Configuration file
 │       └── ...
```

### API Endpoints

| Method | Endpoint         | Description                              | Request Body      | Response Body  |
|--------|------------------|------------------------------------------|-------------------|----------------|
| POST   | `/auth/register` | Register a new user                      | `RegisterRequest` | `AuthResponse` |
| POST   | `/auth/login`    | Authenticate a user and receive a token  | `LoginRequest`    | `AuthResponse` |

### Example Request/Response

**Register (POST `/auth/register`)**

Request:
```json
{
  "username": "john",
  "email": "john@example.com",
  "password": "password123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "john",
  "role": "USER"
}
```

**Login (POST `/auth/login`)**

Request:
```json
{
  "username": "john",
  "password": "password123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "john",
  "role": "USER"
}
```

### Getting Started

#### Prerequisites

- Java 17 or higher
- Maven 3.8+
- IDE (e.g., IntelliJ IDEA, Eclipse)
- Database (configurable via `application.properties`; uses PostgreSQL)

### Configuration

Edit `src/main/resources/application.properties` to configure the database and JWT settings:

```properties
# PostgreSQL DB Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/finance_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

# Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update

# JWT
jwt.secret=your-secret-key-at-least-32-characters-long
jwt.expiration=3600000
```

> The JWT secret must be at least 32 characters for HMAC-SHA256. In production, inject it via an environment variable rather than hardcoding it.

---
### Future Enhancements

This project is actively evolving. The following items are prioritized to make it more secure, scalable, testable, and production-ready.

#### 1. Complete Microservices Architecture
- Finish **Investment Service** (full CRUD + basic portfolio reporting/summaries).
- Implement **Budget Service** as the orchestrator (coordinates Expense and Investment Services).
- Add reliable service-to-service communication (e.g., via **RestTemplate**, **Feign Client**, or **WebClient** for now; async later).

#### 2. Testing Improvements
- Add comprehensive **integration tests** using `@SpringBootTest` + **Testcontainers** for PostgreSQL.
- Expand unit test coverage (aim for 80%+ on services and controllers).

#### 3. Clean Code & Design Documentation
- Apply and enforce **SOLID principles** more explicitly:
   - **Single Responsibility** — already strong with separated concerns.
   - **Open-Closed** — introduce interfaces/abstract classes (e.g., `ReportStrategy` for extensible reporting).
   - **Dependency Inversion** — ensure high-level modules depend on abstractions.
- Implement common **design patterns** (Factory for report generators, Strategy for calculations, Repository via Spring Data).
- Add a new **"Design Decisions"** section in the README explaining 2–3 key principles/patterns with code references.

#### 4. Concurrency & Performance Basics
- Introduce `@Async` methods for non-blocking tasks (e.g., background report generation or email notifications).
- Use `CompletableFuture` for parallel fetches (e.g., combining expense + investment data in Budget Service).
- Document thread-safety measures and potential race conditions handled.

#### 5. Developer Experience & Quick Wins
- Add **Swagger/OpenAPI** documentation using springdoc-openapi (auto-generated interactive docs).
- Implement **caching** on summary/report endpoints with `@Cacheable` (Caffeine or Ehcache).
- Set up **Docker Compose** for easy local multi-service + PostgreSQL spin-up.
- Enhance README with:
   - Architecture diagram (created in draw.io / diagrams.net).
   - Full deployment instructions (including multi-service).
   - Exported Postman collection for API testing.

#### 6. Advanced Scalability (Stretch Goals)
- Replace direct/synchronous service calls with **event-driven communication** using **Kafka** or **RabbitMQ** (e.g., publish expense/investment events for Budget Service to consume).
- Add **API Gateway** (Spring Cloud Gateway) + service discovery (Eureka/Consul).
- Introduce distributed tracing (Micrometer + Zipkin) and monitoring basics.