# Food Delivery System

This is a Java desktop application I originally built as a university OOP project. It simulates a simple food delivery service with separate options for customers, drivers, and administrators.

The original version uses Java Swing and Microsoft SQL Server. I am now moving it step by step to a Spring Boot backend with PostgreSQL, with a React web interface planned for later. The Swing source is still kept temporarily while the backend is being built.

## What the application can do

### Customer

- Create an account and log in
- Browse restaurants and their menus
- Add food to a cart
- Place an order

### Driver

- View orders waiting for a driver
- Accept an order
- Mark a delivery as completed

### Administrator

- Add a restaurant
- Delete a restaurant

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

Flyway creates the restaurant and menu tables automatically when the backend starts.

### 2. Configure the database connection

Set these environment variables before starting the application:

```bash
export DB_URL='jdbc:postgresql://localhost:5432/food_delivery'
export DB_USER='your_database_user'
export DB_PASSWORD='your_database_password'
```

On Windows PowerShell:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/food_delivery"
$env:DB_USER="your_database_user"
$env:DB_PASSWORD="your_database_password"
```

The `.env.example` file contains the same variable names as a reference.

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

- Continue moving the existing features into the Spring Boot backend
- Build a React web interface
- Add secure authentication
- Add more automated tests
