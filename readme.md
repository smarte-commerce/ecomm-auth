# Auth Service

This is a microservice for authentication and authorization, providing comprehensive RESTful APIs for managing accounts, customers, vendors, roles, permissions, and shops.

## Database Schema

The database includes the following main entities:

### Account Credentials
- `accounts` table with fields:
  - `id` (UUID)
  - `username` (String)
  - `password` (String)
  - `status` (Boolean)
  - `role_code` (Enum: AccountType)
  - `last_token` (String)

### Customer
- `customers` table with fields:
  - `id` (UUID, same as account id)
  - `customer_name` (String)
  - `customer_address` (String)
  - `customer_phone` (String)
  - `customer_email` (String)
  - `customer_logo` (String)
  - `customer_status` (Boolean)

### Vendor
- `vendors` table with fields:
  - `id` (UUID, same as account id)
  - `vendor_name` (String)
  - `vendor_description` (String)
  - `vendor_address` (String)
  - `vendor_phone` (String)
  - `vendor_email` (String)
  - `vendor_logo` (String)
  - `vendor_status` (Boolean)

### Permission
- `permissions` table with fields:
  - `id` (UUID)
  - `name` (String)
  - `code` (String)
  - `api_path` (String)
  - `method` (String)
  - `module` (String)
  - `left` (Integer, for hierarchical permissions)
  - `right` (Integer, for hierarchical permissions)

### Role
- `roles` table with fields:
  - `id` (UUID)
  - `role_description` (String)
  - `role_code` (Enum: AccountType)

## RESTful API Endpoints

### Authentication Endpoints

#### `/auth/login`
- `POST`: Login with username and password
  - Request: `LoginRequest` (username, password)
  - Response: `AuthResponse` (account details, tokens)

#### `/auth/register`
- `POST`: Register a new user
  - Request: `RegisterRequest` (username, password, accountType)
  - Response: HTTP 201 Created

#### `/auth/account`
- `GET`: Get current account details
  - Response: `AuthResponse` (account details)

#### `/auth/logout`
- `POST`: Logout current user
  - Response: HTTP 204 No Content

### User Endpoints

#### `/users`
- `GET`: Get all users with pagination
  - Query Params: page, size, sortBy, sortDir
  - Response: `PagedResponse<AccountVm>`
- `POST`: Create a new user
  - Request: `AccountVm`
  - Response: `AccountVm`
- `DELETE`: Delete multiple users
  - Request: List of UUIDs
  - Response: HTTP 204 No Content

#### `/users/{username}`
- `GET`: Get user by username
  - Response: `AccountVm`

#### `/users/id/{id}`
- `GET`: Get user by ID
  - Response: `AccountVm`

#### `/users/{id}`
- `PUT`: Update user
  - Request: `AccountVm`
  - Response: `AccountVm`
- `DELETE`: Delete user
  - Response: HTTP 204 No Content

#### `/users/{id}/activate`
- `PATCH`: Activate user
  - Response: `AccountVm`

#### `/users/{id}/deactivate`
- `PATCH`: Deactivate user
  - Response: `AccountVm`

#### `/users/{accountId}/customer/{customerId}`
- `PATCH`: Assign customer to account
  - Response: `AccountVm`

#### `/users/{accountId}/vendor/{vendorId}`
- `PATCH`: Assign vendor to account
  - Response: `AccountVm`

### Customer Endpoints

#### `/customers`
- `GET`: Get all customers with pagination
  - Query Params: page, size, sortBy, sortDir
  - Response: `PagedResponse<CustomerVm>`
- `POST`: Create a new customer
  - Request: `CustomerVm`
  - Response: `CustomerVm`
- `DELETE`: Delete multiple customers
  - Request: List of UUIDs
  - Response: HTTP 204 No Content

#### `/customers/{id}`
- `GET`: Get customer by ID
  - Response: `CustomerVm`
- `PUT`: Update customer
  - Request: `CustomerVm`
  - Response: `CustomerVm`
- `DELETE`: Delete customer
  - Response: HTTP 204 No Content

#### `/customers/search`
- `GET`: Search customers
  - Query Params: keyword, page, size
  - Response: `PagedResponse<CustomerVm>`

#### `/customers/account/{accountId}`
- `GET`: Get customer by account ID
  - Response: `CustomerVm`

#### `/customers/{id}/activate`
- `PATCH`: Activate customer
  - Response: `CustomerVm`

#### `/customers/{id}/deactivate`
- `PATCH`: Deactivate customer
  - Response: `CustomerVm`

### Vendor Endpoints

#### `/vendors`
- `GET`: Get all vendors with pagination
  - Query Params: page, size, sortBy, sortDir
  - Response: `PagedResponse<VendorVm>`
- `POST`: Create a new vendor
  - Request: `VendorVm`
  - Response: `VendorVm`
- `DELETE`: Delete multiple vendors
  - Request: List of UUIDs
  - Response: HTTP 204 No Content

#### `/vendors/{id}`
- `GET`: Get vendor by ID
  - Response: `VendorVm`
- `PUT`: Update vendor
  - Request: `VendorVm`
  - Response: `VendorVm`
- `DELETE`: Delete vendor
  - Response: HTTP 204 No Content

#### `/vendors/search`
- `GET`: Search vendors
  - Query Params: keyword, page, size
  - Response: `PagedResponse<VendorVm>`

#### `/vendors/account/{accountId}`
- `GET`: Get vendor by account ID
  - Response: `VendorVm`

#### `/vendors/{id}/activate`
- `PATCH`: Activate vendor
  - Response: `VendorVm`

#### `/vendors/{id}/deactivate`
- `PATCH`: Deactivate vendor
  - Response: `VendorVm`

### Role Endpoints

#### `/roles`
- `GET`: Get all roles with pagination
  - Query Params: page, size, sortBy, sortDir
  - Response: `PagedResponse<RoleVm>`
- `POST`: Create a new role
  - Request: `RoleVm`
  - Response: `RoleVm`

#### `/roles/{id}`
- `GET`: Get role by ID
  - Response: `RoleVm`
- `PUT`: Update role
  - Request: `RoleVm`
  - Response: `RoleVm`

#### `/roles/{ids}`
- `DELETE`: Delete multiple roles
  - Response: HTTP 204 No Content

#### `/roles/search`
- `GET`: Search roles
  - Query Params: keyword, page, size
  - Response: `PagedResponse<RoleVm>`

#### `/roles/{id}/permissions`
- `GET`: Get permissions by role ID
  - Response: List of `PermissionVm`

#### `/roles/{roleId}/permissions/{permissionId}`
- `POST`: Assign permission to role
  - Response: `RoleVm`
- `DELETE`: Remove permission from role
  - Response: `RoleVm`

#### `/roles/{roleId}/permissions`
- `POST`: Assign multiple permissions to role
  - Request: List of UUIDs
  - Response: `RoleVm`

### Permission Endpoints

#### `/permissions`
- `GET`: Get all permissions with pagination
  - Query Params: page, size, sortBy, sortDir
  - Response: `PagedResponse<PermissionVm>`
- `POST`: Create a new permission
  - Request: `PermissionVm`
  - Response: `PermissionVm`

#### `/permissions/{id}`
- `GET`: Get permission by ID
  - Response: `PermissionVm`
- `PUT`: Update permission
  - Request: `PermissionVm`
  - Response: `PermissionVm`

#### `/permissions/{ids}`
- `DELETE`: Delete multiple permissions
  - Response: HTTP 204 No Content

#### `/permissions/search`
- `GET`: Search permissions
  - Query Params: keyword, page, size
  - Response: `PagedResponse<PermissionVm>`

#### `/permissions/module/{module}`
- `GET`: Get permissions by module
  - Response: List of `PermissionVm`

#### `/permissions/role/{roleId}`
- `GET`: Get permissions by role ID
  - Response: List of `PermissionVm`

#### `/permissions/{id}/children`
- `GET`: Get child permissions
  - Response: List of `PermissionVm`

#### `/permissions/{childId}/parent/{parentId}`
- `POST`: Assign parent permission
  - Response: `PermissionVm`

### Shop Endpoints

#### `/shops`
- `GET`: Get all shops with pagination
  - Query Params: page, size, sortBy, sortDir
  - Response: `PagedResponse<ShopVm>`
- `POST`: Create a new shop
  - Request: `ShopVm`
  - Response: `ShopVm`
- `DELETE`: Delete multiple shops
  - Request: List of UUIDs
  - Response: HTTP 204 No Content

#### `/shops/{id}`
- `GET`: Get shop by ID
  - Response: `ShopVm`
- `PUT`: Update shop
  - Request: `ShopVm`
  - Response: `ShopVm`
- `DELETE`: Delete shop
  - Response: HTTP 204 No Content

#### `/shops/search`
- `GET`: Search shops
  - Query Params: keyword, page, size
  - Response: `PagedResponse<ShopVm>`

#### `/shops/vendor/{vendorId}`
- `GET`: Get shops by vendor ID
  - Response: List of `ShopVm`

#### `/shops/health`
- `GET`: Health check
  - Response: String

## Recent Updates

### Backend Services Implementation

The service implementations have been fixed to address all linter errors and implement the missing methods:

1. **RoleServiceImpl**
   - Fixed implementation of `RoleServiceInterface` methods
   - Added proper pagination, sorting and search functionality
   - Implemented role-permission relationships (assign, remove, list permissions)
   - Added type-safe conversions between entities and view models

2. **PermissionServiceImpl**
   - Fixed implementation of `PermissionServiceInterface` methods
   - Added hierarchical permission structure with nested set model
   - Implemented permission search by module and by role
   - Added proper pagination and sorting functionality

3. **Entity Relationships**
   - Added many-to-many relationship between roles and permissions
   - Implemented proper hierarchy for permissions using left/right values
   - Ensured proper cascading on relationship changes

These changes ensure proper functioning of the RESTful API endpoints described in this document.
