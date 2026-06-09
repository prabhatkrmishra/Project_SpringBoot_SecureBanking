# 🏦 Bank Security Application
 
A **Spring Boot REST API** for a secure banking system featuring JWT-based authentication, role-based access control, account management, card services, and investment features.
 
---
 
## 📌 Table of Contents
 
- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Features](#features)
- [Getting Started](#getting-started)
- [API Reference & Test JSONs](#api-reference--test-jsons)
  - [User Endpoints](#1-user-endpoints)
  - [Account Endpoints](#2-account-endpoints)
  - [Card Endpoints](#3-card-endpoints)
  - [Investment Endpoints](#4-investment-endpoints)
  - [Admin Endpoints](#5-admin-endpoints)
- [Role & Security Model](#role--security-model)
- [Enums Reference](#enums-reference)
---
 
## Overview
 
The **Bank Security Application** is a backend REST service that simulates core banking operations. It enables customers to register, open bank accounts, manage cards, add nominees, update KYC details, and invest money. An admin panel allows privileged users to manage users and accounts.
 
Security is enforced via **Spring Security + JWT tokens**, with two roles: `ROLE_ADMIN` and `ROLE_CUSTOMER`.
 
---
 
## Tech Stack
 
| Layer        | Technology                          |
|--------------|--------------------------------------|
| Framework    | Spring Boot 2.7.16                   |
| Security     | Spring Security + JWT (jjwt 0.11.5) |
| Persistence  | Spring Data JPA + Hibernate          |
| Database     | MySQL                                |
| Build Tool   | Maven                                |
| Java Version | Java 17                              |
| Utilities    | Lombok                               |
 
---
 
## Project Structure
 
```
com.security.bank
├── config/
│   ├── DataInitializer.java       # Seeds ROLE_ADMIN & ROLE_CUSTOMER on startup
│   └── SecurityConfig.java        # JWT filter chain, BCrypt, auth manager
├── controller/
│   ├── UserController.java
│   ├── UserAccountController.java
│   ├── UserCardController.java
│   ├── UserInvestmentController.java
│   └── AdminController.java
├── dto/                           # Request/Response data transfer objects
├── entity/                        # JPA entities + Enums
├── exception/
│   └── ResourceNotFoundException.java
├── repository/                    # Spring Data JPA repositories
├── security/
│   ├── CustomUserDetailService.java
│   ├── JwtAuthenticationFilter.java
│   └── JwtAuthenticationHelper.java
├── service/                       # Service interfaces
└── service/impl/                  # Service implementations
```
 
---
 
## Features
 
- **User Registration & JWT Login**
- **Role-Based Access Control** (`ROLE_CUSTOMER`, `ROLE_ADMIN`)
- **Account Management** — create SAVINGS / CURRENT / PPF / SALARY accounts with auto-assigned branch, interest rate, and card
- **Card Management** — auto-generated card number & CVV, block card, apply for new card, update PIN & daily limit
- **Nominee Management** — add/update nominee per account
- **KYC Management** — view and update KYC details via account number
- **Investment Module** — invest in GOLD, STOCKS, MUTUAL_FUND, FIXED_DEPOSITS; balance deducted automatically
- **Admin Panel** — manage users, activate/deactivate accounts, filter accounts by type or branch
---
 
## Getting Started
 
### 1. Configure MySQL
 
Create a database and update `src/main/resources/application.properties`:
 
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bank_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
 
### 2. Build & Run
 
```bash
mvn clean install
mvn spring-boot:run
```
 
The application runs at `http://localhost:8080`.  
On startup, `ROLE_ADMIN` and `ROLE_CUSTOMER` are automatically seeded into the database.
 
### 3. Authentication Flow
 
All protected endpoints require a Bearer token in the `Authorization` header:
 
```
Authorization: Bearer <your_jwt_token>
```
 
Obtain a token by calling `POST /user/login`.
 
---
 
## API Reference & Test JSONs
 
---
 
### 1. User Endpoints
 
#### `POST /user/register` — Register a new customer
 
No auth required.
 
**Request Body:**
```json
{
  "name": "Rahul Sharma",
  "username": "rahul123",
  "password": "password@123",
  "address": "12, MG Road, Bangalore",
  "number": 9876543210,
  "identityProof": "AADHAR-1234-5678"
}
```
 
**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Rahul Sharma",
  "username": "rahul123",
  "address": "12, MG Road, Bangalore",
  "number": 9876543210,
  "identityProof": "AADHAR-1234-5678",
  "roles": {
    "id": 2,
    "roleName": "ROLE_CUSTOMER"
  }
}
```
 
---
 
#### `POST /user/login` — Login and get JWT token
 
No auth required.
 
**Request Body:**
```json
{
  "username": "rahul123",
  "password": "password@123"
}
```
 
**Response (200 OK):**
```json
{
  "jwtToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYWh1bDEyMyIsImlhdCI6..."
}
```
 
> Use this token in all subsequent requests as: `Authorization: Bearer <jwtToken>`
 
---
 
### 2. Account Endpoints
 
> Requires `ROLE_CUSTOMER` + valid JWT token.
 
#### `POST /account/create/{userId}` — Create a bank account
 
**URL:** `/account/create/1`
 
**Request Body (SAVINGS account with nominee):**
```json
{
  "accountType": "SAVINGS",
  "balance": 50000.00,
  "proof": "AADHAR-1234-5678",
  "nominee": {
    "name": "Priya Sharma",
    "relation": "Sister",
    "accountNumber": 1234567890,
    "gender": "Female",
    "age": 25
  }
}
```
 
**Request Body (CURRENT account):**
```json
{
  "accountType": "CURRENT",
  "balance": 100000.00,
  "proof": "PAN-ABCDE1234F"
}
```
 
**Request Body (PPF account):**
```json
{
  "accountType": "PPF",
  "balance": 20000.00,
  "proof": "PASSPORT-A1234567"
}
```
 
**Request Body (SALARY account):**
```json
{
  "accountType": "SALARY",
  "balance": 75000.00,
  "proof": "AADHAR-9876-5432"
}
```
 
**Response (201 Created):**
```json
{
  "id": 1,
  "accountType": "SAVINGS",
  "status": "ACTIVE",
  "balance": 50000.0,
  "interestRate": 2.7,
  "branch": "BOB",
  "proof": "AADHAR-1234-5678",
  "openingDate": "2025-01-01",
  "accountNumber": 48372916,
  "nominee": {
    "id": 1,
    "name": "Priya Sharma",
    "relation": "Sister",
    "accountNumber": 1234567890,
    "gender": "Female",
    "age": 25
  },
  "card": {
    "id": 1,
    "cardNumber": 4837291600123456,
    "cardHolderName": "Rahul Sharma",
    "cardType": "DEBIT_GLOBAL",
    "dailyLimit": 40000.0,
    "cvv": 472,
    "allocationDate": "2025-01-01",
    "expiryDate": "2030-01-01",
    "pin": 1122,
    "status": "ACTIVE"
  }
}
```
 
**Account Type → Auto-assigned Values:**
 
| Account Type | Interest Rate | Branch | Card Type      | Daily Limit |
|-------------|---------------|--------|----------------|-------------|
| SAVINGS     | 2.70%         | BOB    | DEBIT_GLOBAL   | ₹40,000     |
| CURRENT     | 5.20%         | ICIC   | CREDIT_PREMIUM | ₹50,000     |
| PPF         | 7.40%         | SBI    | *(no card)*    | —           |
| SALARY      | 4.10%         | HDFC   | CREDIT_MASTER  | ₹75,000     |
 
---
 
#### `GET /account/all/{userId}` — Get all accounts of a user
 
**URL:** `/account/all/1`  
No body needed.
 
---
 
#### `GET /account/balance?accountNumber=48372916` — Get account balance
 
---
 
#### `GET /account/nominee?accountNumber=48372916` — Get nominee details
 
---
 
#### `PUT /account/updateNominee/{accountId}` — Update nominee
 
**URL:** `/account/updateNominee/1`
 
**Request Body:**
```json
{
  "name": "Amit Sharma",
  "relation": "Brother",
  "accountNumber": 9876543210,
  "gender": "Male",
  "age": 30
}
```
 
---
 
#### `GET /account/getKycDetails?accountNumber=48372916` — Get KYC details
 
**Response:**
```json
{
  "name": "Rahul Sharma",
  "address": "12, MG Road, Bangalore",
  "number": 9876543210,
  "identityProof": "AADHAR-1234-5678"
}
```
 
---
 
#### `PUT /account/updateKyc/{accountId}` — Update KYC
 
**URL:** `/account/updateKyc/1`
 
**Request Body:**
```json
{
  "name": "Rahul Kumar Sharma",
  "address": "45, Park Street, Mumbai",
  "number": 9988776655,
  "identityProof": "PAN-XYZAB1234C"
}
```
 
---
 
#### `GET /account/getAccount/summary?accountNumber=48372916` — Get account summary
 
Returns the full account object with the `user` field set to null.
 
---
 
### 3. Card Endpoints
 
> Requires `ROLE_CUSTOMER` + valid JWT token.
 
#### `GET /card/block?accountNumber=48372916&cardNumber=4837291600123456` — Block a card
 
**Response:** `"Card Blocked Successfully"`
 
---
 
#### `POST /card/apply/new?accountNumber=48372916` — Apply for a new card
 
(Use after blocking an existing card, or for PPF accounts which don't get one on creation.)
 
**Request Body:**
```json
{
  "cardHolderName": "Rahul Sharma",
  "cardType": "DEBIT_CLASSIC",
  "pin": 4321
}
```
 
> Valid `cardType` values: `DEBIT_CLASSIC`, `DEBIT_GLOBAL`, `CREDIT_PREMIUM`, `CREDIT_MASTER`
 
**Response:** `"New Card Allocated to account with Number: 48372916"`
 
---
 
#### `PUT /card/setting?cardNumber=4837291600123456` — Update card settings
 
**Request Body (update PIN and daily limit):**
```json
{
  "pin": 9999,
  "dailyLimit": 35000
}
```
 
**Daily Limit Caps by Card Type:**
 
| Card Type      | Maximum Daily Limit |
|----------------|---------------------|
| DEBIT_CLASSIC  | ₹40,000             |
| DEBIT_GLOBAL   | ₹50,000             |
| CREDIT_PREMIUM | ₹75,000             |
| CREDIT_MASTER  | ₹1,00,000           |
 
---
 
### 4. Investment Endpoints
 
> Requires `ROLE_CUSTOMER` + valid JWT token.
 
#### `POST /invest/now?accountId=1` — Invest from an account
 
**Request Body (Gold investment):**
```json
{
  "investmentType": "GOLD",
  "amount": 10000.00,
  "duration": "2 years"
}
```
 
**Request Body (Stocks):**
```json
{
  "investmentType": "STOCKS",
  "amount": 25000.00,
  "duration": "1 year"
}
```
 
**Request Body (Mutual Fund):**
```json
{
  "investmentType": "MUTUAL_FUND",
  "amount": 15000.00,
  "duration": "3 years"
}
```
 
**Request Body (Fixed Deposit):**
```json
{
  "investmentType": "FIXED_DEPOSITS",
  "amount": 50000.00,
  "duration": "5 years"
}
```
 
**Response:** `"Investment successful"`
 
**Investment Type → Auto-assigned Values:**
 
| Investment Type | Risk   | Returns | Company               |
|-----------------|--------|---------|-----------------------|
| GOLD            | LOW    | 8.0%    | Gold Trust            |
| STOCKS          | HIGH   | 18.0%   | NSE Equity            |
| MUTUAL_FUND     | MEDIUM | 12.0%   | SBI Mutual Fund       |
| FIXED_DEPOSITS  | LOW    | 7.0%    | Fixed Deposit Scheme  |
 
---
 
### 5. Admin Endpoints
 
> Requires `ROLE_ADMIN` + valid JWT token.  
> First create an admin via `POST /admin/add` (can be done by an existing admin).
 
#### `POST /admin/add` — Register an admin
 
**Request Body:**
```json
{
  "name": "Super Admin",
  "username": "admin001",
  "password": "admin@secure123",
  "address": "Head Office, Delhi",
  "number": 9911223344,
  "identityProof": "EMP-ID-ADM-001"
}
```
 
---
 
#### `GET /admin/getAllUser` — Get all users
 
---
 
#### `GET /admin/getUserByName/{username}` — Get user by username
 
**URL:** `/admin/getUserByName/rahul123`
 
---
 
#### `DELETE /admin/deleteUser/{userId}` — Delete a user
 
**URL:** `/admin/deleteUser/1`  
**Response:** `"Deleted Successfully"` or `"Error in deletion"`
 
---
 
#### `PUT /admin/account/deactivate?userId=1&accountId=1` — Deactivate an account
 
**Response:** `"Deactivated Account for User with id: 1"` or `"ERROR"`
 
---
 
#### `PUT /admin/account/activate?userId=1&accountId=1` — Activate an account
 
**Response:** `"Activated Account for User with id: 1"` or `"ERROR"`
 
---
 
#### `GET /admin/account/getActiveAccountsList` — Get all ACTIVE accounts
 
---
 
#### `GET /admin/account/getInActiveAccountsList` — Get all INACTIVE accounts
 
---
 
#### `GET /admin/accountList/ByAccountType/{accType}` — Filter accounts by type
 
**URL examples:**
- `/admin/accountList/ByAccountType/SAVINGS`
- `/admin/accountList/ByAccountType/CURRENT`
- `/admin/accountList/ByAccountType/PPF`
- `/admin/accountList/ByAccountType/SALARY`
---
 
#### `GET /admin/accountList/ByBranchType/{branchType}` — Filter accounts by branch
 
**URL examples:**
- `/admin/accountList/ByBranchType/SBI`
- `/admin/accountList/ByBranchType/ICIC`
- `/admin/accountList/ByBranchType/BOB`
- `/admin/accountList/ByBranchType/HDFC`
---
 
## Role & Security Model
 
| Endpoint Pattern         | Access          |
|--------------------------|-----------------|
| `POST /user/register`    | Public          |
| `POST /user/login`       | Public          |
| `/admin/**`              | ROLE_ADMIN only |
| All other endpoints      | Any authenticated user (ROLE_CUSTOMER) |
 
JWT tokens are valid for **5 hours** from issue time.
 
---
 
## Enums Reference
 
```
AccountType:   SAVINGS | CURRENT | PPF | SALARY
BranchType:    SBI | ICIC | BOB | HDFC
CardType:      DEBIT_CLASSIC | DEBIT_GLOBAL | CREDIT_PREMIUM | CREDIT_MASTER
InvestmentType: GOLD | STOCKS | MUTUAL_FUND | FIXED_DEPOSITS
```
 
---
 
## 🧪 Suggested Testing Flow in Postman
 
1. `POST /user/register` — create a customer
2. `POST /user/login` — get JWT token → copy it
3. Set `Authorization: Bearer <token>` in Postman headers
4. `POST /account/create/{userId}` — open a SAVINGS account
5. `GET /account/balance?accountNumber=...` — check balance
6. `POST /invest/now?accountId=...` — make an investment
7. `GET /card/block?accountNumber=...&cardNumber=...` — block the card
8. `POST /card/apply/new?accountNumber=...` — apply for a new card
9. `POST /admin/add` — (as admin) create an admin account
10. Login as admin → use admin endpoints to manage users & accounts

---
 
> **Owner:** [Prabhat Kumar Mishra @prabhatkrmishra](https://github.com/prabhatkrmishra)
 
---