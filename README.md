# N11 Final Project - Fullstack E-Commerce Microservices

This project is a fullstack e-commerce application developed with Spring Boot microservices and React.js.  
The backend includes product listing, cart management, order creation, Iyzico payment integration, JWT authentication, email verification, RabbitMQ messaging, Docker/Jib containerization, GitHub Actions CI, and Slack notifications.

## Tech Stack

- Java 17
- Spring Boot
- Spring Security & JWT
- Spring Data JPA
- PostgreSQL
- RabbitMQ
- Eureka Service Discovery
- Spring Cloud Gateway
- Iyzico Sandbox
- Java Mail Sender
- Swagger/OpenAPI
- JUnit & Mockito
- Docker & Docker Compose
- Jib Maven Plugin
- GitHub Actions
- Slack Incoming Webhook
- AWS Elastic Beanstalk & RDS PostgreSQL

## Architecture

```txt
React Frontend
      |
      v
API Gateway
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

RabbitMQ:
- EmailVerificationEvent
- PaymentResultEvent

Databases:
- PostgreSQL per service