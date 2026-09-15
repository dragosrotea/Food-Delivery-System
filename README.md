# Food Delivery System

This is a Java food delivery application that I originally built as a university OOP project. The original version uses Java Swing and Microsoft SQL Server. I am now moving it step by step to a Spring Boot backend with PostgreSQL, with a React web interface planned for later.

The Swing source is still kept temporarily while its features are moved to the backend.

## What the backend can do

### Customer

- Create an account and log in
- Browse active restaurants and available menu items
- Place and cancel orders
- View personal order history

### Administrator

- Add and update restaurants and menu items
- Activate or deactivate restaurants and menu items
- View all orders and advance their status

Driver functionality is planned as the next backend feature.

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
- Java Swing and Microsoft SQL Server in the original version

## Project structure

```text
src/
├── main/
│   ├── java/
│   │   ├── com/dragosrotea/fooddelivery/
│   │   ├── database/
│   │   ├── exceptions/
│   │   ├── model/
│   │   ├── services/
│   │   └── view/
│   └── resources/
│       └── db/migration/
└── test/
    ├── java/
    └── resources/
```

The new backend uses Spring Boot, JPA, and PostgreSQL. The original Swing source remains in the repository while its features are moved to the backend.

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

- Add the driver order workflow
- Build a React web interface
- Add Docker and PostgreSQL integration tests
