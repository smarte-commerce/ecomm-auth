# Keycloak Integration Setup Guide

This guide explains how to set up Keycloak integration with the auth service.

## Prerequisites

- Keycloak server running (version 24.x or higher)
- Java 21+
- Maven 3.6+

## Keycloak Server Setup

### 1. Start Keycloak Server

```bash
# Using Docker
docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:24.0.1 start-dev

# Or using standalone server
cd keycloak-24.0.1
bin/kc.sh start-dev
```

### 2. Create Realm

1. Access Keycloak Admin Console: `http://localhost:8080`
2. Login with admin/admin
3. Create a new realm called `microservice`
4. Configure realm settings as needed

### 3. Create Client

1. In the `microservice` realm, go to Clients
2. Create a new client with:
   - **Client ID**: `auth-service`
   - **Client Type**: `OpenID Connect`
   - **Client authentication**: `On` (for confidential client)
3. Configure client settings:
   - **Valid redirect URIs**: 
     - `http://localhost:8092/auth/oauth2/callback`
     - `http://localhost:3000/*` (for frontend)
   - **Web origins**: `http://localhost:8092`, `http://localhost:3000`
   - **Standard flow enabled**: `On`
   - **Direct access grants enabled**: `On`
   - **Service accounts enabled**: `On` (optional)

### 4. Client Credentials

1. Go to client's `Credentials` tab
2. Copy the `Client secret`
3. Update your application.yaml with the secret

## Application Configuration

### Environment Variables

Set the following environment variables:

```bash
export KEYCLOAK_SERVER_URL=http://localhost:8080
export KEYCLOAK_REALM=microservice
export KEYCLOAK_CLIENT_ID=auth-service
export KEYCLOAK_CLIENT_SECRET=your-client-secret-here
export KEYCLOAK_ADMIN_USERNAME=admin
export KEYCLOAK_ADMIN_PASSWORD=admin
```

### Application.yaml Configuration

The auth service is already configured to use these values. Make sure your `application.yaml` contains:

```yaml
keycloak:
  server-url: ${KEYCLOAK_SERVER_URL:http://localhost:8080}
  realm: ${KEYCLOAK_REALM:microservice}
  client-id: ${KEYCLOAK_CLIENT_ID:auth-service}
  client-secret: ${KEYCLOAK_CLIENT_SECRET:your-client-secret}
  admin:
    username: ${KEYCLOAK_ADMIN_USERNAME:admin}
    password: ${KEYCLOAK_ADMIN_PASSWORD:admin}
  direct-access-grants-enabled: true

spring:
  security:
    oauth2:
      client:
        registration:
          keycloak:
            client-id: ${KEYCLOAK_CLIENT_ID:auth-service}
            client-secret: ${KEYCLOAK_CLIENT_SECRET:your-client-secret}
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            scope: openid,profile,email
        provider:
          keycloak:
            issuer-uri: ${KEYCLOAK_SERVER_URL:http://localhost:8080}/realms/${KEYCLOAK_REALM:microservice}
      resourceserver:
        jwt:
          issuer-uri: ${KEYCLOAK_SERVER_URL:http://localhost:8080}/realms/${KEYCLOAK_REALM:microservice}
```

## Authentication Flows

### 1. Direct Access (Username/Password)

```bash
curl -X POST http://localhost:8092/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password"
  }'
```

### 2. OAuth2 Authorization Code Flow

1. **Get Authorization URL**:
```bash
curl http://localhost:8092/auth/oauth2/authorize?redirectUri=http://localhost:8092/auth/oauth2/callback
```

2. **Redirect user to the returned URL**
3. **User authenticates with Keycloak**
4. **Keycloak redirects to callback with authorization code**
5. **Service exchanges code for tokens automatically**

### 3. Token Refresh

```bash
curl -X POST http://localhost:8092/auth/refresh \
  -H "Cookie: refresh_token=your-refresh-token"
```

## User Registration

Users can be registered through the service, and they will be created in both the local database and Keycloak:

```bash
curl -X POST http://localhost:8092/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "password": "password123",
    "email": "user@example.com",
    "fullName": "New User",
    "phone": "+1234567890",
    "accountType": "CUSTOMER",
    "address": "123 Main St"
  }'
```

## Frontend Integration

### Getting Authorization URL

```javascript
const response = await fetch('/auth/oauth2/authorize?redirectUri=http://localhost:3000/callback');
const authUrl = await response.text();
window.location.href = authUrl;
```

### Handling Callback

The OAuth2 callback will redirect to your frontend with tokens set as HTTP-only cookies.

### Checking Authentication Status

```javascript
const response = await fetch('/auth/status', {
  credentials: 'include'
});
const status = await response.json();
if (status.authenticated) {
  console.log('User is logged in:', status.username);
}
```

## Troubleshooting

### Common Issues

1. **Invalid Client**: Check client ID and secret in Keycloak
2. **Redirect URI Mismatch**: Ensure redirect URIs are configured in Keycloak client
3. **CORS Issues**: Check CORS configuration in SecurityConfig
4. **JWT Validation Errors**: Verify Keycloak realm and issuer URI

### Logs

Enable debug logging for OAuth2 and Security:

```yaml
logging:
  level:
    org.springframework.security: DEBUG
    org.springframework.security.oauth2: DEBUG
    com.winnguyen1905.auth: DEBUG
```

### Testing Keycloak Connection

```bash
# Test Keycloak availability
curl http://localhost:8080/realms/microservice/.well-known/openid_configuration

# Test client credentials
curl -X POST http://localhost:8080/realms/microservice/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials&client_id=auth-service&client_secret=your-secret"
```

## Security Considerations

1. **Use HTTPS in production**
2. **Secure client secrets** (use environment variables)
3. **Configure proper CORS** for your frontend domains
4. **Set appropriate token expiration times**
5. **Enable additional security features** in Keycloak (MFA, rate limiting, etc.)

## Migration from Custom JWT

The service supports both Keycloak and local JWT authentication. To disable local JWT and use only Keycloak:

```yaml
keycloak:
  direct-access-grants-enabled: true
```

To disable Keycloak and use only local JWT:

```yaml
keycloak:
  direct-access-grants-enabled: false
``` 
