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
 │   │   ├── ExpenseServiceApplication.java      # Main application entry point
 │   │   ├── controller/
 │   │   │   └── ExpenseController.java         # REST controller for expense endpoints
 │   │   ├── dto/
 │   │   │   ├── CreateExpenseRequest.java      # DTO for creating/updating expenses
 │   │   │   └── ExpenseResponse.java           # DTO for expense response data
 │   │   ├── model/
 │   │   │   └── Expense.java                   # Entity model for expenses
 │   │   ├── repository/
 │   │   │   └── ExpenseRepository.java         # JPA repository for expense persistence
 │   │   ├── service/
 │   │   │   └── ExpenseService.java            # Business logic for expense operations
 │   └── resources/
 │       ├── application.properties             # Configuration file
 │       └── ...
 │       
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

| Method | Endpoint                     | Description                              | Request Body                     | Response Body                     |
|--------|------------------------------|------------------------------------------|----------------------------------|-----------------------------------|
| GET    | `/api/expense`              | Retrieve all expenses                   | None                             | List of `ExpenseResponse`         |
| GET    | `/api/expense/{id}`         | Retrieve expense by ID                  | None                             | `ExpenseResponse`                 |
| GET    | `/api/expense/category`     | Retrieve expenses by category           | Query param: `category`          | List of `ExpenseResponse`         |
| GET    | `/api/expense/summary`      | Retrieve expenses for a specific month  | Query params: `year`, `month`    | List of `ExpenseResponse`         |
| POST   | `/api/expense`              | Create a new expense                    | `CreateExpenseRequest`           | `ExpenseResponse`                 |
| PUT    | `/api/expense/{id}`         | Update an existing expense              | `CreateExpenseRequest`           | `ExpenseResponse`                 |
| DELETE | `/api/expense/{id}`         | Delete an expense by ID                 | None                             | None (204 No Content)             |

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
   - Replace <your-dockerhub-username> with your Docker Hub username.
   - Make sure the image: field in expense-deployment.yaml matches.


5. Load the Docker Image:

- Option 1 Push the Image to Docker Hub

   ```bash
   docker build -t expense-api .
   docker tag expense-api <your-dockerhub-username>/expense-api:latest
   docker push <your-dockerhub-username>/expense-api:latest
   ```

  - Replace <your-dockerhub-username> with your Docker Hub username.
  - Make sure the image: field in expense-deployment.yaml matches.
  
- Option 2 Load the Docker Image into Kind

   If you prefer not to use Docker Hub, you can load the local expense-api image into Kind:

   ```bash
   docker build -t expense-api .
   kind load docker-image expense-api:latest --name <cluster-name>
   ```

  

   **Important**: Replace <your-dockerhub-username>/expense-api:latest with your Docker Hub username in the image field. If you used the local image with kind load docker-image, use image: expense-api:latest instead.


6. Deploy the Spring Boot app:
   ```bash
   kubectl apply -f k8s/expense-deployment.yaml
   kubectl apply -f k8s/expense-service.yaml
   ```

7. Expose via NodePort (or Ingress):
   Your expense-service.yaml exposes the app on port 30080:

   ```bash
   kubectl get svc expense-api-service
   ```

   - You can now access the app at:http://<node-ip>:30080


8. For Kind or other local clusters, check the node’s IP:
   ```bash
   kubectl get nodes -o wide
   ```

   - Use the INTERNAL-IP of a node.

9. Access the API at:
   ```bash
   http://<node-ip>:30080/api/expense
   ```
### Future Enhancements
- Add Global Exception Handling
- Use Lombok for Boilerplate
- Add Spring Security for authentication and role-based access control.
- Deploy services using Docker and Kubernetes.
- Introduce an API Gateway for routing and load balancing.
- Add Swagger/OpenAPI documentation for API endpoints.
- Implement event-driven communication between services using Kafka or RabbitMQ.


### Budget Service (Planned)

The Budget Service will allow users to define budgets for specific months or years, integrating with the Expense and Investment Services to track spending and investment allocations.

### Investment Service (Planned)

The Investment Service will manage investment records, including creating, updating, and retrieving investment details, with integration into the overall budgeting process.
