# Food Delivery System

A full-stack food delivery application developed from a university OOP project. The project now uses a Spring Boot and PostgreSQL backend with a React frontend under active development.

## What the application can do

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

- Java 17 and Spring Boot
- PostgreSQL, JPA, Hibernate and Flyway
- React, TypeScript and Vite
- Maven and npm
- JUnit 5
- GitHub Actions
- OpenAPI and Swagger UI

## Project structure

```text
frontend/                         React application
src/main/java/.../fooddelivery/  Spring Boot backend
src/main/resources/db/migration/ Flyway migrations
src/test/                        Backend tests
```

```text
React -> Spring Boot -> JPA/Hibernate -> PostgreSQL
```

## Run the backend

You need JDK 17 or newer, Maven and PostgreSQL.

Create an empty PostgreSQL database named `food_delivery`, then set:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/food_delivery"
$env:DB_USER="your_database_user"
$env:DB_PASSWORD="your_database_password"
$env:JWT_SECRET="replace-with-a-secret-that-is-at-least-32-bytes-long"
```

To create the initial administrator account, optionally set both `APP_ADMIN_EMAIL` and `APP_ADMIN_PASSWORD`. The root `.env.example` lists all backend variables.

Start the backend:

```bash
mvn clean verify
mvn spring-boot:run
```

The health endpoint is available at `http://localhost:8080/api/health` and Swagger UI at `http://localhost:8080/swagger-ui.html`.

## Run the frontend

You need Node.js 22.12 or newer. Keep the backend running, then open another terminal:

```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`. The frontend API URL defaults to `http://localhost:8080`; copy `frontend/.env.example` to `frontend/.env` if you need to change it.

## Future improvements

- Add authentication and role-aware navigation to the React interface
- Add customer, driver and administrator frontend workflows
- Add Docker and PostgreSQL integration tests
- Add real-time order status updates
