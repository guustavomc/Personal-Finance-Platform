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
     ├── java/com/example/expense
     │   ├── ExpenseServiceApplication.java      # Main application entry point
     │   ├── controller/
     │   │   └── ExpenseController.java         # REST controller for expense endpoints
     │   ├── dto/
     │   │   ├── CreateExpenseRequest.java      # DTO for creating/updating expenses
     │   │   └── ExpenseResponse.java           # DTO for expense response data
     │   ├── model/
     │   │   └── Expense.java                   # Entity model for expenses
     │   ├── repository/
     │   │   └── ExpenseRepository.java         # JPA repository for expense persistence
     │   ├── service/
     │   │   └── ExpenseService.java            # Business logic for expense operations
     └── resources/
         ├── application.properties             # Configuration file
         └── ...
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

### Budget Service (Planned)

The Budget Service will allow users to define budgets for specific months or years, integrating with the Expense and Investment Services to track spending and investment allocations.

### Investment Service (Planned)

The Investment Service will manage investment records, including creating, updating, and retrieving investment details, with integration into the overall budgeting process.

### Getting Started

#### Prerequisites

- Java 17 or higher
- Maven 3.8+
- IDE (e.g., IntelliJ IDEA, Eclipse)
- Database (configurable via `application.properties`; defaults to H2 in-memory for development)

#### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/personal-finance-platform.git
   cd personal-finance-platform
   ```

   - Use tools like Postman or cURL to test the API endpoints. Unit and integration tests can be added using JUnit and Spring Boot Test.

   - For local testing of Expense-Service functionality, we can run PostgreSQL with docker-compose, and app as a container

2. Start the DB locally:
   ```bash
   docker-compose -f db-docker-compose.yml up -d
   ```
3. Build the JAR and Docker image
   ```bash
   ./mvnw clean package -DskipTests
   docker build -t expense-api:latest .
   ```

4. Test the Docker Container:
   ```bash
    docker run -p 8080:8080 expense-api
   ```

   - Maps port 8080 on the host to 8080 in the container.


5. Access the API at `http://localhost:8080/api/expense`.

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

### Future Enhancements

- Implement Budget and Investment Services.
- Add Spring Security for authentication and role-based access control.
- Deploy services using Docker and Kubernetes.
- Introduce an API Gateway for routing and load balancing.
- Add Swagger/OpenAPI documentation for API endpoints.
- Implement event-driven communication between services using Kafka or RabbitMQ.

## Contributing

Contributions are welcome! Please fork the repository, create a feature branch, and submit a pull request with your changes. Ensure code follows the existing style and includes tests where applicable.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact

For questions or feedback, please reach out to [your-email@example.com](mailto:your-email@example.com).