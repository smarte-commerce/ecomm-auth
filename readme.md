# Authentication Service

## Overview

The Authentication Service is responsible for user identity and access management across the platform. It provides JWT token issuance and validation, user registration and management, and role-based access control.

## Key Features

- OAuth2/OIDC compliant authorization server
- JWT token issuance and validation
- User account registration and management
- Role-based access control (RBAC)
- Integration with external identity providers
- Secure password handling
- Account recovery workflows

## Technical Stack

- Spring Boot 3.x
- Spring Security with OAuth2 Resource Server
- JSON Web Tokens (JWT)
- JPA/Hibernate for data persistence
- PostgreSQL database

## Project Structure

- `/src/main/java/com/winnguyen1905/auth/`
  - `/aspect`: AOP aspects for cross-cutting concerns
  - `/common`: Common utilities and constants
  - `/config`: Configuration classes including OAuth2 and security
  - `/core`: Core domain logic and services
  - `/exception`: Custom exception classes and handlers
  - `/persistance`: Data persistence layer
  - `/util`: Utility classes

## API Endpoints

- `POST /api/v1/auth/register`: User registration
- `POST /api/v1/auth/login`: User login and token issuance
- `POST /api/v1/auth/refresh`: Token refresh
- `POST /api/v1/auth/validate`: Token validation
- `GET /api/v1/auth/me`: Get current user information
- `POST /api/v1/auth/logout`: User logout

## Getting Started

### Prerequisites
- Java 21
- Maven 3.8+
- PostgreSQL database

### Setup
1. Configure database connection in `application.yaml`
2. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Docker
To run with Docker:
```bash
docker build -t auth-service .
docker run -p 8080:8080 auth-service
```

## Integration with Other Services

The Authentication Service is a critical component that integrates with all other services in the platform for authorization and identity verification. Other services communicate with the Auth Service to validate JWTs and retrieve user information.

## Documentation

For more detailed documentation, see:
- API Documentation: `/swagger-ui.html` (when running)
- Security Configuration: `com.winnguyen1905.auth.config.SecurityConfig`
