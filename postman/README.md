# Auth Service API - Postman Collection

This folder contains comprehensive Postman collections for testing the Authentication and Authorization microservice.

## Files Included

- `Auth-Service-API.postman_collection.json` - Complete API collection with all endpoints
- `Auth-Service-Environment.postman_environment.json` - Environment variables for different deployment scenarios
- `README.md` - This documentation file

## Collection Overview

The Auth Service API collection includes the following endpoint groups:

### 1. Authentication
- **Login** - Authenticate user with username/password
- **Register** - Register new user account
- **Get Account Info** - Retrieve current user's account information
- **Logout** - Logout and invalidate tokens
- **Refresh Token** - Refresh access token using refresh token
- **Auth Status** - Check authentication status

### 2. OAuth2 / Keycloak Integration
- **Get Authorization URL** - Get OAuth2 authorization URL for Keycloak redirect
- **OAuth2 Callback** - Handle OAuth2 callback and exchange code for tokens

### 3. User Management
- **Get User by Username** - Retrieve user by username
- **Get User by ID** - Retrieve user by UUID
- **Get All Users** - Paginated list of all users
- **Search Users** - Search users by keyword
- **Create User** - Create new user account
- **Update User** - Update user information
- **Delete User** - Delete single user
- **Delete Multiple Users** - Bulk delete users
- **Activate/Deactivate User** - Enable/disable user accounts
- **Assign Customer/Vendor to Account** - Link customer or vendor profiles

### 4. Role Management
- **Create Role** - Create new role
- **Get Role by ID** - Retrieve role information
- **Get All Roles** - Paginated list of roles
- **Search Roles** - Search roles by keyword
- **Update Role** - Update role information
- **Delete Roles** - Delete one or multiple roles
- **Get Permissions by Role** - Get all permissions for a role
- **Assign/Remove Permissions** - Manage role-permission relationships

### 5. Permission Management
- **Get Permission by ID** - Retrieve permission information
- **Get All Permissions** - Paginated list of permissions
- **Search Permissions** - Search permissions by keyword
- **Get Permissions by Module** - Filter permissions by module
- **Get Permissions by Role** - Get permissions assigned to a role
- **Create Permission** - Create new permission
- **Update Permission** - Update permission information
- **Delete Permissions** - Delete one or multiple permissions
- **Get Child Permissions** - Get hierarchical child permissions
- **Assign Parent Permission** - Set permission hierarchy

## Setup Instructions

### 1. Import Collections
1. Open Postman
2. Click "Import" button
3. Select both JSON files:
   - `Auth-Service-API.postman_collection.json`
   - `Auth-Service-Environment.postman_environment.json`

### 2. Set Environment
1. In Postman, select "Auth Service Environment" from the environment dropdown
2. Update environment variables as needed for your deployment

### 3. Configure Base URL
Update the `baseUrl` environment variable to match your service deployment:
- Local development: `http://localhost:8092`
- Docker: `http://localhost:8092` (if using default port mapping)
- Production: Update to your production URL

## Environment Variables

| Variable | Description | Default Value |
|----------|-------------|---------------|
| `baseUrl` | Auth service base URL | `http://localhost:8092` |
| `keycloakUrl` | Keycloak server URL | `http://localhost:8080` |
| `accessToken` | JWT access token (auto-populated) | |
| `refreshToken` | JWT refresh token (auto-populated) | |
| `username` | Test username | `testuser123` |
| `password` | Test password | `password123` |
| `email` | Test email | `testuser@example.com` |
| `redirectUri` | OAuth2 redirect URI | `http://localhost:8092/auth/oauth2/callback` |
| `frontendUrl` | Frontend application URL | `http://localhost:3000` |

## Authentication Flow

### 1. Standard Login Flow
1. Use "Register" to create a new account (if needed)
2. Use "Login" with username/password
3. Copy the `accessToken` from response to `accessToken` environment variable
4. Copy the `refreshToken` from response to `refreshToken` environment variable
5. Use authenticated endpoints with the Bearer token

### 2. OAuth2 Flow
1. Use "Get Authorization URL" to get Keycloak authorization URL
2. Open the URL in browser to authenticate with Keycloak
3. Copy the authorization code from callback URL
4. Use "OAuth2 Callback" with the authorization code
5. Tokens will be set in cookies automatically

## Sample Data

### Register Request
```json
{
  "username": "newuser123",
  "password": "password123",
  "email": "newuser@example.com",
  "phone": "+1234567890",
  "fullName": "John Doe",
  "accountType": "CUSTOMER",
  "address": "123 Main Street, City, State"
}
```

### Account Types
- `ADMIN` - Administrator access
- `VENDOR` - Vendor/seller access
- `CUSTOMER` - Customer/buyer access

### Permission Structure
```json
{
  "name": "Create Product",
  "code": "CREATE_PRODUCT",
  "apiPath": "/api/products",
  "method": "POST",
  "module": "PRODUCT"
}
```

### Role Structure
```json
{
  "name": "Product Manager",
  "code": "PRODUCT_MANAGER"
}
```

## Testing Scenarios

### 1. User Registration and Authentication
1. Register a new user with `CUSTOMER` account type
2. Login with the new credentials
3. Verify account information
4. Test token refresh
5. Logout

### 2. Admin User Management
1. Login as admin user
2. Create new users with different account types
3. Search and filter users
4. Activate/deactivate user accounts
5. Assign roles to users

### 3. Role and Permission Management
1. Create new permissions for different modules
2. Create roles and assign permissions
3. Test permission inheritance
4. Verify role-based access control

### 4. OAuth2 Integration
1. Get authorization URL
2. Authenticate with Keycloak
3. Handle callback with authorization code
4. Verify token exchange

## Error Handling

The API returns standard HTTP status codes:
- `200` - Success
- `201` - Created
- `400` - Bad Request
- `401` - Unauthorized
- `403` - Forbidden
- `404` - Not Found
- `500` - Internal Server Error

Error responses include detailed messages:
```json
{
  "message": "Authentication failed",
  "status": 401,
  "timestamp": "2025-01-08T00:00:00Z"
}
```

## Security Considerations

1. **Tokens**: Access tokens are short-lived (default 1 hour), refresh tokens are longer-lived
2. **HTTPS**: Use HTTPS in production environments
3. **CORS**: Configure CORS settings for frontend integration
4. **Rate Limiting**: Consider implementing rate limiting for authentication endpoints
5. **Input Validation**: All inputs are validated according to defined constraints

## Troubleshooting

### Common Issues

1. **401 Unauthorized**: Check if access token is valid and not expired
2. **403 Forbidden**: Verify user has required permissions for the endpoint
3. **400 Bad Request**: Check request body format and required fields
4. **Connection Refused**: Verify service is running on specified port

### Token Refresh
If you receive 401 errors, try refreshing the token:
1. Use "Refresh Token" endpoint
2. Update `accessToken` environment variable
3. Retry the failed request

### Keycloak Issues
1. Verify Keycloak is running and accessible
2. Check realm configuration
3. Verify client ID and secret
4. Ensure redirect URIs are properly configured

## Development Notes

- Service runs on port 8092 by default
- Keycloak integration requires proper realm setup
- Database migrations are handled automatically
- Service supports reactive programming with WebFlux
- JWT tokens include user roles and permissions

## Support

For issues or questions:
1. Check service logs for detailed error information
2. Verify environment configuration
3. Test with minimal request data
4. Check network connectivity to dependent services

## Version History

- v1.0 - Initial collection with basic auth endpoints
- v1.1 - Added OAuth2/Keycloak integration
- v1.2 - Added comprehensive user, role, and permission management
- v1.3 - Added environment variables and documentation
