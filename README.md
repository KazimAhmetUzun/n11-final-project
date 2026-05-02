# KAU61 - N11 Final Project Fullstack E-Commerce Application

KAU61 is a fullstack e-commerce application developed as the N11 Final Project. It includes a React frontend and a Spring Boot microservices backend. The project demonstrates a real-world e-commerce flow with authentication, email verification, product listing, cart management, order creation, Iyzico sandbox payment integration, asynchronous event handling, stock management, CI pipeline, Slack notifications, Dockerized local development, Jib image builds, and an AWS deployment example.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Services and Ports](#services-and-ports)
- [Core Features](#core-features)
- [Frontend Branding](#frontend-branding)
- [Application Flows](#application-flows)
- [Running the Project Locally](#running-the-project-locally)
- [Product Seed Data](#product-seed-data)
- [API Endpoints](#api-endpoints)
- [Swagger Documentation](#swagger-documentation)
- [Jib Image Build](#jib-image-build)
- [CI/CD and Slack Notification](#cicd-and-slack-notification)
- [AWS Deployment Example](#aws-deployment-example)
- [Iyzico Sandbox Test Card](#iyzico-sandbox-test-card)
- [Future Improvements](#future-improvements)

---

## Project Overview

The project follows a microservice-based e-commerce architecture. Each domain is separated into an independent service, and services communicate through REST APIs and RabbitMQ events.

The application supports the following main user journey:

```text
Register
  -> Verify Email
  -> Login
  -> View Products
  -> Add Product to Cart
  -> Create Order
  -> Pay with Iyzico Sandbox
  -> Order becomes PAID
  -> Product stock decreases
```

---

## Architecture

```text
React Frontend
      |
      v
Spring Cloud API Gateway
      |
      v
Eureka Service Discovery
      |
      +--> auth-service
      +--> product-service
      +--> cart-service
      +--> order-service
      +--> payment-service
      +--> notification-service

RabbitMQ Events:
- EmailVerificationEvent
- PaymentResultEvent

Database:
- PostgreSQL database per service
```

### Event-Driven Communication

RabbitMQ is used for asynchronous communication between services:

- `auth-service` publishes an email verification event.
- `notification-service` consumes the email verification event and sends the verification email.
- `payment-service` publishes payment result events.
- `order-service` consumes payment result events and updates order status.

---

## Tech Stack

### Frontend

- React
- Vite
- React Router
- Axios
- CSS
- LocalStorage-based JWT handling

### Backend

- Java 17
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- PostgreSQL
- RabbitMQ
- Eureka Service Discovery
- Spring Cloud Gateway
- Iyzico Sandbox Payment
- Java Mail Sender
- Swagger / OpenAPI
- JUnit 5
- Mockito

### DevOps and Cloud

- Docker
- Docker Compose
- Jib Maven Plugin
- GitHub Actions
- Slack Incoming Webhook
- AWS Elastic Beanstalk
- AWS RDS PostgreSQL

---

## Services and Ports

| Service | Port | Description |
|---|---:|---|
| discovery-server | 8761 | Eureka service discovery |
| api-gateway | 8080 | Single entry point for backend APIs |
| auth-service | 8081 | Register, login, JWT, email verification |
| product-service | 8082 | Product listing, pagination, stock management |
| cart-service | 8083 | Cart operations |
| order-service | 8084 | Order creation and order status management |
| payment-service | 8085 | Iyzico sandbox payment integration |
| notification-service | 8086 | Email notification service |
| RabbitMQ Management | 15672 | RabbitMQ dashboard |
| frontend | 5173 | React frontend |

---

## Core Features

### User Features

- User registration
- Email verification
- Login and logout
- Product listing with pagination
- Product detail page
- Add product to cart
- Update cart item quantity
- Remove item from cart
- Clear cart
- Create order
- View user orders
- Continue payment for `CREATED` orders
- Pay with Iyzico sandbox card
- View order detail

### Backend Features

- JWT-based authentication
- Email verification with RabbitMQ
- Product pagination
- Stock decrease after successful payment
- Choreography-based Saga flow
- Iyzico sandbox payment integration
- Unit tests
- Swagger documentation
- Logging
- Dockerized services
- Jib image build without Dockerfile
- GitHub Actions CI pipeline
- Slack notification for CI results
- AWS Elastic Beanstalk + RDS deployment example

---

## Frontend Branding

The frontend uses custom branding:

```text
KAU61
N 11 Final Project Fullstack E-Commerce Application
```

Theme colors:

| Color | Hex |
|---|---|
| Navy | `#0b2a5b` |
| Burgundy | `#7a1630` |
| Light Background | `#f4f6fb` |

The project logo is stored under:

```text
frontend/src/assets/kau61-logo.png
```

---

## Application Flows

### Authentication Flow

```text
User registers
   |
   v
auth-service creates user with emailVerified=false
   |
   v
EmailVerificationEvent is published to RabbitMQ
   |
   v
notification-service sends verification email
   |
   v
User enters verification code
   |
   v
Email is verified
   |
   v
User logs in
   |
   v
JWT token and user email are stored in localStorage
```

Frontend stores the following values:

```text
token
userEmail
```

After login, cart, order, and payment requests use the logged-in user's email.

---

### Product Pagination Flow

The product listing page displays 10 products per page.

```http
GET http://localhost:8080/api/products?page=0&size=12
```

Expected response structure:

```json
{
  "content": [],
  "page": 0,
  "size": 10,
  "totalElements": 24,
  "totalPages": 3,
  "last": false
}
```

Product listing uses stable ID sorting so products do not move between pages after stock updates.

---

### Cart and Order Flow

```text
Product Detail
   |
   v
Add to Cart
   |
   v
Cart Page
   |
   v
Create Order
   |
   v
Order status = CREATED
   |
   v
Payment Page
```

If an order remains in `CREATED` status, the user can continue the payment from the **My Orders** page.

---

### Payment and Saga Flow

```text
payment-service
   |
   v
Iyzico Sandbox
   |
   v
PaymentResultEvent
   |
   v
RabbitMQ
   |
   v
order-service
   |
   +--> SUCCESS: Order status becomes PAID
   |             Product stock decreases
   |
   +--> FAILED: Order status becomes CANCELLED
```

The payment amount is not entered manually by the user. The frontend reads the amount from `order.totalPrice`, and the backend validates that the payment request amount matches the order total before calling Iyzico.

If the request amount does not match the order total, payment is rejected.

---

### Stock Decrease Flow

```text
order-service receives PaymentResultEvent
   |
   v
Order status becomes PAID
   |
   v
order-service calls product-service
   |
   v
Product stock decreases
```

Stock decrease endpoint:

```http
PATCH /api/products/{id}/decrease-stock?quantity=2
```

---

## Running the Project Locally

### 1. Clone the Repository

```bash
git clone <repository-url>
cd n11-final-project
```

---

### 2. Create Backend Environment File

Create the following file:

```text
backend/.env
```

Example content:

```env
IYZICO_API_KEY=your_iyzico_api_key
IYZICO_SECRET_KEY=your_iyzico_secret_key

MAIL_HOST=smtp.gmail.com
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_gmail_app_password
```

Do not push `.env` to GitHub.

`.gitignore` should include:

```gitignore
.env
backend/.env
```

---

### 3. Start Backend Services

```bash
cd backend
docker compose up -d
```

Check running containers:

```bash
docker ps
```

Eureka dashboard:

```text
http://localhost:8761
```

RabbitMQ dashboard:

```text
http://localhost:15672
```

Default RabbitMQ credentials:

```text
guest / guest
```

---

### 4. Start Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend URL:

```text
http://localhost:5173
```

---

## Product Seed Data

The project includes 24 sample products for product listing and pagination.

Seed file:

```text
backend/database/product-seed.sql
```

Import seed data into Docker PostgreSQL:

```bash
cd backend
docker exec -i product-db psql -U postgres -d product_db < database/product-seed.sql
```

Expected output:

```text
TRUNCATE TABLE
INSERT 0 24
```

Check product count:

```bash
docker exec -i product-db psql -U postgres -d product_db -c "SELECT COUNT(*) FROM products;"
```

---

## API Endpoints

Base URL:

```text
http://localhost:8080
```

### Auth

```http
POST /api/auth/register
POST /api/auth/verify-email
POST /api/auth/login
```

### Products

```http
GET    /api/products?page=0&size=12
GET    /api/products/{id}
PATCH  /api/products/{id}/decrease-stock?quantity=2
```

### Cart

```http
POST   /api/cart
GET    /api/cart/{userEmail}
PUT    /api/cart/{id}
DELETE /api/cart/{id}
DELETE /api/cart/clear/{userEmail}
```

### Orders

```http
POST /api/orders
GET  /api/orders/{id}
GET  /api/orders/user/{userEmail}
```

### Payments

```http
POST /api/payments
GET  /api/payments/user/{userEmail}
GET  /api/payments/order/{orderId}
```

---

## Swagger Documentation

Swagger UI is available for backend services.

Examples:

```text
http://localhost:8081/swagger-ui.html
http://localhost:8082/swagger-ui.html
http://localhost:8083/swagger-ui.html
http://localhost:8084/swagger-ui.html
http://localhost:8085/swagger-ui.html
```

---

## Jib Image Build

Services use Jib to build Docker images without Dockerfiles.

Example:

```bash
cd backend/product-service
./mvnw compile jib:dockerBuild
```

After rebuilding an image, recreate the related container:

```bash
cd backend
docker rm -f product-service
docker compose up -d product-service
```

---

## CI/CD and Slack Notification

GitHub Actions workflow file:

```text
.github/workflows/backend-ci.yml
```

Pipeline steps:

```text
1. Checkout repository
2. Set up Java 17
3. Run backend unit tests
4. Build backend services
5. Send Slack notification
```

The build job runs only after tests pass.

Slack Incoming Webhook is used for CI result notifications.

GitHub secret:

```text
SLACK_WEBHOOK_URL
```

Notification examples:

```text
Backend CI successful
Backend CI failed
```

---

## AWS Deployment Example

As an AWS deployment example, `product-service` was deployed to AWS Elastic Beanstalk and connected to AWS RDS PostgreSQL.

```text
product-service
   |
   v
AWS Elastic Beanstalk
   |
   v
AWS RDS PostgreSQL product_db
```

AWS-specific configuration:

```text
application-aws.yml
SPRING_PROFILES_ACTIVE=aws
DB_HOST
DB_USERNAME
DB_PASSWORD
server.port=5000
```

This demonstrates that the microservice architecture can be deployed to AWS. Other services can be deployed using the same strategy.

---

## Iyzico Sandbox Test Card

```text
Card Number: 5528790000000008
Expire Month: 12
Expire Year: 2030
CVC: 123
Card Holder: John Doe
```

---

## Example User Flow

```text
1. User registers
2. User verifies email
3. User logs in
4. User views product list
5. User opens product detail
6. User adds product to cart
7. User creates order
8. User pays with Iyzico sandbox card
9. PaymentResultEvent is published
10. Order becomes PAID
11. Product stock decreases
12. User views order detail
```

---

## Future Improvements

- Admin product management panel
- Product search and filtering
- Coupon and discount system
- Google login
- Keycloak integration
- Centralized logging
- Monitoring dashboard
- Full AWS deployment for all services
- Frontend CI/CD pipeline
- Frontend deployment

---

## Project Status

Current status:

```text
Backend microservices: completed
Frontend core user flow: completed
Docker Compose local environment: completed
GitHub Actions backend CI: completed
Slack notification: completed
AWS product-service deployment example: completed
```
