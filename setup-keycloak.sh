#!/bin/bash

# Wait for Keycloak to be ready
echo "Waiting for Keycloak to be ready..."
sleep 10

# Get admin token
ADMIN_TOKEN=$(curl -s -X POST "http://localhost:8087/realms/master/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin" \
  -d "password=admin" \
  -d "grant_type=password" \
  -d "client_id=admin-cli" | jq -r '.access_token')

echo "Admin token: ${ADMIN_TOKEN:0:20}..."

# Create microservice realm
echo "Creating microservice realm..."
curl -s -X POST "http://localhost:8087/admin/realms" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "realm": "microservice",
    "enabled": true,
    "displayName": "Microservice Realm"
  }'

# Create auth-service client
echo "Creating auth-service client..."
curl -s -X POST "http://localhost:8087/admin/realms/microservice/clients" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "auth-service",
    "enabled": true,
    "clientAuthenticatorType": "client-secret",
    "secret": "auth-service-secret",
    "redirectUris": ["http://localhost:8092/*"],
    "webOrigins": ["http://localhost:8092"],
    "publicClient": false,
    "protocol": "openid-connect",
    "standardFlowEnabled": true,
    "directAccessGrantsEnabled": true
  }'

echo "Keycloak setup completed!"
echo "Realm: microservice"
echo "Client ID: auth-service" 
echo "Client Secret: auth-service-secret"
echo "Keycloak Admin URL: http://localhost:8087"
echo "Username: admin"
echo "Password: admin"
