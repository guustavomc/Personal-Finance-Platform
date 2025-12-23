# Personal-Finance-Platform

A RESTful API platform for managing personal finances, built with Spring Boot. This project enables users to create and manage monthly/yearly budgets, track expenses, and handle investments through a microservices architecture. The platform is designed to be scalable, testable, and extensible, with plans to incorporate Spring Security for authentication and authorization in future iterations.

## Project Overview

The Personal-Finance-Platform consists of three microservices:

- **Budget Service**: Responsible for creating and managing budgets for specific months or years. It orchestrates interactions with the Expense and Investment Services.
- **Expense Service**: Manages expense records, including creating, retrieving, updating, and deleting expenses. This service is called directly by the Budget Service.
- **Investment Service**: Handles investment tracking and management, also called directly by the Budget Service.

Currently, only the **Expense Service** is implemented. The Budget and Investment Services are planned for future development.

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

| Method | Endpoint                               | Description                                      | Request Body                  | Response Body                   |
|--------|----------------------------------------|--------------------------------------------------|-------------------------------|---------------------------------|
| GET    | `/api/expense`                      | Retrieve all expenses                            | None                          | List of `ExpenseResponse`       |
| GET    | `/api/expense/{id}`                    | Retrieve expense by ID                           | None                          | List of `ExpenseResponse`               |
| GET    | `/api/expense/category`                | Retrieve expenses by category                    | Query param: `category`       | List of `ExpenseResponse`       |
| POST   | `/api/expense`                         | Create a new expense                             | `CreateExpenseRequest`        |List of `ExpenseResponse`              |
| PUT    | `/api/expense/{id}`                    | Update an existing expense                       | `CreateExpenseRequest`        | List of `ExpenseResponse`                |
| DELETE | `/api/expense/{id}`                    | Delete an expense by ID                          | None                          | None (204 No Content)           |
| GET    | `/api/expense/report/summary/monthly`  | Retrieve  Summarized expenses for a specific month | Query params: `year`, `month` | `ExpenseSummaryResponse`        |
| GET    | `/api/expense/report/summary/annual`   | Retrieve  Summarized expenses for a specific year | Query params: `year`          | `ExpenseSummaryResponse`        |
| GET    | `/api/expense/report/detailed/monthly` | Retrieve  List of expenses for a specific month  | Query params: `year`, `month` | List of `ExpenseResponse`       |
| GET    | `/api/expense/report/detailed/annual`  | Retrieve  List of expenses for a specific year  | Query params: `year`          | List of `ExpenseResponse`       |

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


### Future Enhancements
- Add Global Exception Handling
- Use Lombok for Boilerplate
- Add Spring Security for authentication and role-based access control.
- Introduce an API Gateway for routing and load balancing.
- Add Swagger/OpenAPI documentation for API endpoints.
- Implement event-driven communication between services using Kafka or RabbitMQ.


### Budget Service (Planned)

The Budget Service will allow users to define budgets for specific months or years, integrating with the Expense and Investment Services to track spending and investment allocations.

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
