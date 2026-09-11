# Food Delivery System

This is a Java desktop application I originally built as a university OOP project. It simulates a simple food delivery service with separate options for customers, drivers, and administrators.

The current version uses Java Swing for the interface and Microsoft SQL Server for storing the data. I am now improving the original project step by step, starting with a cleaner Maven setup and proper automated tests. A web version with Spring Boot and React is planned for later.

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
- Java Swing
- Microsoft SQL Server
- JDBC
- Maven
- JUnit 5
- GitHub Actions

## Project structure

```text
src/
├── main/
│   ├── java/
│   │   ├── database/
│   │   ├── exceptions/
│   │   ├── model/
│   │   ├── services/
│   │   └── view/
│   └── resources/db/migration/
└── test/java/model/
```

The interface calls the service classes, the services handle the application logic, and the DAO classes communicate with the database.

```text
Swing interface -> Services -> DAOs -> SQL Server
```

## How to run it

You need:

- JDK 17 or newer
- Maven
- Microsoft SQL Server
- `sqlcmd` or another way to run SQL scripts

### 1. Create the database

Run the two scripts in this order:

```bash
sqlcmd -S localhost -U <database-user> -P <database-password> -i src/main/resources/db/migration/V1__create_schema.sql
sqlcmd -S localhost -U <database-user> -P <database-password> -i src/main/resources/db/migration/V2__seed_demo_data.sql
```

The second script adds a few restaurants, menu items, and demo accounts.

| Role | Username | Password |
|---|---|---|
| Customer | `customer_demo` | `customer_demo` |
| Driver | `driver_demo` | `driver_demo` |
| Admin | `admin_demo` | `admin_demo` |

These accounts are only for testing the application locally. The current school-project version still stores passwords as plain text, which will be replaced with password hashing when authentication is rebuilt with Spring Security.

### 2. Configure the database connection

Set these environment variables before starting the application:

```bash
export DB_URL='jdbc:sqlserver://localhost:1433;databaseName=FoodDeliveryDB;encrypt=true;trustServerCertificate=true'
export DB_USER='your_database_user'
export DB_PASSWORD='your_database_password'
```

On Windows PowerShell, the same variables can be set like this:

```powershell
$env:DB_URL="jdbc:sqlserver://localhost:1433;databaseName=FoodDeliveryDB;encrypt=true;trustServerCertificate=true"
$env:DB_USER="your_database_user"
$env:DB_PASSWORD="your_database_password"
```

The `.env.example` file contains the same variable names as a quick reference.

### 3. Build and start the application

```bash
mvn clean verify
mvn exec:java
```

The first command compiles the project and runs the JUnit tests. The second command opens the Swing application.

## Current limitations

This is still the first desktop version of the project. Some important improvements are planned:

- Hash passwords and properly protect each user role
- Use `BigDecimal` instead of `double` for prices
- Improve validation and error handling
- Add more unit and database tests
- Replace the Swing interface with a React frontend
- Rebuild the Java backend using Spring Boot and REST endpoints
- Make the whole application easier to run with Docker

## Original documentation

The reports made for the university assignment are available in [docs/coursework](docs/coursework).
