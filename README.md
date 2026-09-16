# Food Delivery System

A Java food delivery backend developed from a university OOP project and rebuilt with Spring Boot and PostgreSQL. A React web interface is planned as the next stage.

## What the backend can do

### Customer

- Create an account and log in
- Browse active restaurants and available menu items
- Place and cancel orders
- View personal order history

### Driver

- Log in with an account created by an administrator
- View orders ready for pickup
- Accept an available delivery
- View assigned deliveries
- Mark an assigned delivery as completed

### Administrator

- Create driver accounts
- Add and update restaurants and menu items
- Activate or deactivate restaurants and menu items
- View all orders and advance preparation statuses

## Technologies used

- Java 17
- Spring Boot
- PostgreSQL
- JPA and Hibernate
- Flyway
- Maven
- JUnit 5
- GitHub Actions
- OpenAPI and Swagger UI

## Project structure

```text
src/
├── main/
│   ├── java/com/dragosrotea/fooddelivery/
│   │   ├── auth/
│   │   ├── common/
│   │   ├── config/
│   │   ├── driver/
│   │   ├── order/
│   │   ├── restaurant/
│   │   ├── security/
│   │   └── user/
│   └── resources/db/migration/
└── test/
    ├── java/
    └── resources/
```

```text
Spring Boot -> JPA/Hibernate -> PostgreSQL
```

## How to run it

You need:

- JDK 17 or newer
- Maven
- PostgreSQL

### 1. Create the database

Create an empty PostgreSQL database named `food_delivery`:

```bash
createdb -U postgres food_delivery
```

Flyway creates and updates the application tables automatically when the backend starts.

### 2. Configure the application

Set these required environment variables before starting the application:

```bash
export DB_URL='jdbc:postgresql://localhost:5432/food_delivery'
export DB_USER='your_database_user'
export DB_PASSWORD='your_database_password'
export JWT_SECRET='replace-with-a-secret-that-is-at-least-32-bytes-long'
```

On Windows PowerShell:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/food_delivery"
$env:DB_USER="your_database_user"
$env:DB_PASSWORD="your_database_password"
$env:JWT_SECRET="replace-with-a-secret-that-is-at-least-32-bytes-long"
```

The React development origin defaults to `http://localhost:5173`. Set `APP_CORS_ALLOWED_ORIGINS` if the frontend uses another origin.

To create the initial administrator account, optionally set both `APP_ADMIN_EMAIL` and `APP_ADMIN_PASSWORD`. The `.env.example` file lists all supported variables.

### 3. Build and start the backend

```bash
mvn clean verify
mvn spring-boot:run
```

After it starts, open `http://localhost:8080/api/health`. The response should be:

```json
{"status":"UP"}
```

Interactive API documentation is available at `http://localhost:8080/swagger-ui.html`.

## Future improvements

- Build a React web interface
- Add Docker and PostgreSQL integration tests
- Add real-time order status updates
