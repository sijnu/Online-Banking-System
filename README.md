# Online Banking System (React + Spring Boot + MySQL)

A basic but functional online banking system with role-based access (ADMIN, USER), core banking operations, loan workflows, and JWT authentication.

## Tech Stack
- Frontend: React (Vite), TypeScript, Axios
- Backend: Spring Boot (Java 17+), Spring Security (JWT), JPA/Hibernate, Maven
- Database: MySQL 8+

## Project Structure
```
.
├─ backend/
│  ├─ src/main/java/com/example/banking/
│  │  ├─ OnlineBankingApplication.java
│  │  ├─ config/
│  │  ├─ controller/
│  │  ├─ dto/
│  │  ├─ entity/
│  │  ├─ repository/
│  │  └─ service/
│  ├─ src/main/resources/
│  │  └─ application.properties
│  └─ pom.xml
├─ frontend/
│  ├─ index.html
│  ├─ package.json
│  ├─ tsconfig.json
│  ├─ vite.config.ts
│  └─ src/
│     ├─ main.tsx
│     ├─ App.tsx
│     ├─ components/
│     ├─ pages/
│     └─ api/
└─ db/
   └─ schema.sql
```

## Features
- Admin
  - Login with predefined credentials (cannot register)
  - Add users (email, password, username, full name, Aadhaar, initial balance)
  - View all users
  - View all transactions (all or by user)
  - View and approve/reject loan requests
- User
  - Login (created by Admin)
  - Deposit, Withdraw, Transfer
  - Check balance
  - View own transactions
  - Apply for loans and track status

## Database Schema (MySQL)
See `db/schema.sql` for DDL. Tables: `users`, `accounts`, `transactions`, `loans`.

## Prerequisites
- Java 17+
- Maven 3.9+
- Node.js 18+
- MySQL 8+

## Setup

### 1) Database
- Create a MySQL database, e.g. `online_banking`.
- Update DB credentials in `backend/src/main/resources/application.properties`.
- Apply schema:
```bash
mysql -u <user> -p online_banking < db/schema.sql
```

### 2) Backend
```bash
cd backend
mvn spring-boot:run
```
- The API runs on `http://localhost:8080`.

### 3) Frontend
```bash
cd frontend
npm install
npm run dev
```
- The app runs on `http://localhost:5173`.

## Authentication
- JWT-based authentication.
- Login endpoints issue a token. Include header:
```
Authorization: Bearer <token>
```
- Roles: `ADMIN`, `USER`.

## API (High-Level)
- Auth: `/auth/login`
- Admin:
  - `POST /admin/add-user`
  - `GET /admin/users`
  - `GET /admin/transactions`
  - `GET /admin/transactions/{userId}`
  - `GET /admin/loans`
  - `PUT /admin/loans/{loanId}/approve`
  - `PUT /admin/loans/{loanId}/reject`
- User:
  - `POST /user/deposit`
  - `POST /user/withdraw`
  - `POST /user/transfer`
  - `GET /user/balance`
  - `GET /user/transactions`
  - `POST /user/loan/apply`
  - `GET /user/loan/status`

## Environment Variables (Optional)
You can override defaults via environment variables:
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `JWT_SECRET`, `JWT_EXPIRATION_MS`

## Dev Notes
- Passwords are stored using BCrypt.
- For first run, an admin user is auto-seeded.
- CORS is enabled for local frontend dev.

## License
MIT 