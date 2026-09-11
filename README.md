# Food Delivery System

This is a Java desktop application I originally built as a university OOP project. It simulates a simple food delivery service with separate options for customers, drivers, and administrators.

The original version uses Java Swing for the interface and Microsoft SQL Server for storing the data. I am now moving it step by step to a Spring Boot backend, with a React web interface planned for later. The Swing source is still kept temporarily while the backend is being built.

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
│   │   ├── com/dragosrotea/fooddelivery/
│   │   ├── database/
│   │   ├── exceptions/
│   │   ├── model/
│   │   ├── services/
│   │   └── view/
│   └── resources/db/migration/
└── test/java/
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

### 3. Build and start the backend

```bash
mvn clean verify
mvn spring-boot:run
```

After it starts, open `http://localhost:8080/api/health`. The response should be:

```json
{"status":"UP"}
```

## Future improvements

- Continue moving the existing features into the Spring Boot backend
- Build a React web interface
- Add secure authentication
- Add more automated tests
