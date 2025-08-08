package com.winnguyen1905.auth.core.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winnguyen1905.auth.common.constant.AccountType;
import com.winnguyen1905.auth.core.model.request.LoginRequest;
import com.winnguyen1905.auth.core.model.request.RegisterRequest;
import com.winnguyen1905.auth.core.service.KeycloakService;
import com.winnguyen1905.auth.exception.BaseException;
import com.winnguyen1905.auth.util.TokenPair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

  private final WebClient.Builder webClientBuilder;
  private final ObjectMapper objectMapper;

  @Value("${keycloak.server-url}")
  private String keycloakServerUrl;

  @Value("${keycloak.realm}")
  private String realm;

  @Value("${keycloak.client-id}")
  private String clientId;

  @Value("${keycloak.client-secret}")
  private String clientSecret;

  @Value("${keycloak.admin.username}")
  private String adminUsername;

  @Value("${keycloak.admin.password}")
  private String adminPassword;

  private WebClient getWebClient() {
    return webClientBuilder
        .baseUrl(keycloakServerUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .build();
  }

  @Override
  public Mono<TokenPair> authenticateWithKeycloak(LoginRequest loginRequest) {
    log.info("Authenticating user {} with Keycloak", loginRequest.username());

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "password");
    formData.add("client_id", clientId);
    formData.add("client_secret", clientSecret);
    formData.add("username", loginRequest.username());
    formData.add("password", loginRequest.password());
    formData.add("scope", "openid profile email");

    return getWebClient()
        .post()
        .uri("/realms/{realm}/protocol/openid-connect/token", realm)
        .body(BodyInserters.fromFormData(formData))
        .retrieve()
        .onStatus(status -> !status.is2xxSuccessful(),
            clientResponse -> clientResponse.bodyToMono(String.class)
                .flatMap(errorBody -> {
                  log.error("Keycloak authentication failed: {}", errorBody);
                  return Mono.error(new BaseException("Authentication failed", 401));
                }))
        .bodyToMono(String.class)
        .flatMap(response -> {
          try {
            JsonNode jsonNode = objectMapper.readTree(response);
            String accessToken = jsonNode.get("access_token").asText();
            String refreshToken = jsonNode.get("refresh_token").asText();

            return Mono.just(TokenPair.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
          } catch (Exception e) {
            log.error("Error parsing Keycloak response", e);
            return Mono.error(new BaseException("Error parsing authentication response", 500));
          }
        })
        .doOnSuccess(tokens -> log.info("Successfully authenticated user {} with Keycloak", loginRequest.username()))
        .doOnError(error -> log.error("Failed to authenticate user {} with Keycloak", loginRequest.username(), error));
  }

  public Mono<com.winnguyen1905.auth.core.model.response.AuthResponse> authenticateWithKeycloakFullResponse(
      LoginRequest loginRequest) {
    log.info("Authenticating user {} with Keycloak (full response)", loginRequest.username());
    log.debug("Keycloak config - Server URL: {}, Realm: {}, Client ID: {}, Client Secret: {}", 
              keycloakServerUrl, realm, clientId, clientSecret != null ? "[REDACTED]" : "null");

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "password");
    formData.add("client_id", clientId);
    if (clientSecret != null && !clientSecret.isEmpty()) {
      formData.add("client_secret", clientSecret);
    }
    formData.add("username", loginRequest.username());
    formData.add("password", loginRequest.password());
    formData.add("scope", "openid profile email");

    return getWebClient()
        .post()
        .uri("/realms/{realm}/protocol/openid-connect/token", realm)
        .body(BodyInserters.fromFormData(formData))
        .retrieve()
        .onStatus(status -> !status.is2xxSuccessful(),
            clientResponse -> clientResponse.bodyToMono(String.class)
                .flatMap(errorBody -> {
                  log.error("Keycloak authentication failed with status {}: {}", clientResponse.statusCode(), errorBody);
                  log.error("Request details - Client ID: {}, Realm: {}, Server URL: {}", clientId, realm, keycloakServerUrl);
                  return Mono.error(new BaseException("Authentication failed: " + errorBody, clientResponse.statusCode().value()));
                })) 
        .bodyToMono(String.class)
        .flatMap(response -> {
          try {
            JsonNode jsonNode = objectMapper.readTree(response);
            String accessToken = jsonNode.get("access_token").asText();
            String refreshToken = jsonNode.get("refresh_token").asText();
            String idToken = jsonNode.has("id_token") ? jsonNode.get("id_token").asText() : null;
            int expiresIn = jsonNode.has("expires_in") ? jsonNode.get("expires_in").asInt() : 3600;
            int refreshExpiresIn = jsonNode.has("refresh_expires_in") ? jsonNode.get("refresh_expires_in").asInt()
                : 1800;
            String tokenType = jsonNode.has("token_type") ? jsonNode.get("token_type").asText() : "Bearer";
            String scope = jsonNode.has("scope") ? jsonNode.get("scope").asText() : "openid profile email";

            return Mono.just(com.winnguyen1905.auth.core.model.response.AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .idToken(idToken)
                .expiresIn(expiresIn)
                .refreshExpiresIn(refreshExpiresIn)
                .tokenType(tokenType)
                .scope(scope)
                .build());
          } catch (Exception e) {
            log.error("Error parsing Keycloak response", e);
            return Mono.error(new BaseException("Error parsing authentication response", 500));
          }
        })
        .doOnSuccess(
            authResponse -> log.info("Successfully authenticated user {} with Keycloak", loginRequest.username()))
        .doOnError(error -> log.error("Failed to authenticate user {} with Keycloak", loginRequest.username(), error));
  }

  @Override
  public Mono<Void> registerUserInKeycloak(RegisterRequest registerRequest) {
    log.info("Registering user {} in Keycloak", registerRequest.username());

    return getAdminAccessToken()
        .flatMap(adminToken -> {
          Map<String, Object> userRepresentation = createUserRepresentation(registerRequest);

          return getWebClient()
              .post()
              .uri("/admin/realms/{realm}/users", realm)
              .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .bodyValue(userRepresentation)
              .retrieve()
              .onStatus(status -> !status.is2xxSuccessful(),
                  clientResponse -> clientResponse.bodyToMono(String.class)
                      .flatMap(errorBody -> {
                        log.error("Failed to register user in Keycloak: {}", errorBody);
                        return Mono.error(new BaseException("User registration failed", 400));
                      }))
              .bodyToMono(Void.class);
        })
        .doOnSuccess(v -> log.info("Successfully registered user {} in Keycloak", registerRequest.username()))
        .doOnError(error -> log.error("Failed to register user {} in Keycloak", registerRequest.username(), error));
  }

  @Override
  public Mono<TokenPair> refreshToken(String refreshToken) {
    log.info("Refreshing token with Keycloak");

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "refresh_token");
    formData.add("client_id", clientId);
    formData.add("client_secret", clientSecret);
    formData.add("refresh_token", refreshToken);

    return getWebClient()
        .post()
        .uri("/realms/{realm}/protocol/openid-connect/token", realm)
        .body(BodyInserters.fromFormData(formData))
        .retrieve()
        .onStatus(status -> !status.is2xxSuccessful(),
            clientResponse -> clientResponse.bodyToMono(String.class)
                .flatMap(errorBody -> {
                  log.error("Token refresh failed: {}", errorBody);
                  return Mono.error(new BaseException("Token refresh failed", 401));
                }))
        .bodyToMono(String.class)
        .flatMap(response -> {
          try {
            JsonNode jsonNode = objectMapper.readTree(response);
            String accessToken = jsonNode.get("access_token").asText();
            String newRefreshToken = jsonNode.get("refresh_token").asText();

            return Mono.just(TokenPair.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build());
          } catch (Exception e) {
            log.error("Error parsing token refresh response", e);
            return Mono.error(new BaseException("Error parsing token refresh response", 500));
          }
        });
  }

  public Mono<com.winnguyen1905.auth.core.model.response.AuthResponse> refreshTokenFullResponse(String refreshToken) {
    log.info("Refreshing token with Keycloak (full response)");

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "refresh_token");
    formData.add("client_id", clientId);
    formData.add("client_secret", clientSecret);
    formData.add("refresh_token", refreshToken);

    return getWebClient()
        .post()
        .uri("/realms/{realm}/protocol/openid-connect/token", realm)
        .body(BodyInserters.fromFormData(formData))
        .retrieve()
        .onStatus(status -> !status.is2xxSuccessful(),
            clientResponse -> clientResponse.bodyToMono(String.class)
                .flatMap(errorBody -> {
                  log.error("Token refresh failed: {}", errorBody);
                  return Mono.error(new BaseException("Token refresh failed", 401));
                }))
        .bodyToMono(String.class)
        .flatMap(response -> {
          try {
            JsonNode jsonNode = objectMapper.readTree(response);
            String accessToken = jsonNode.get("access_token").asText();
            String newRefreshToken = jsonNode.get("refresh_token").asText();
            String idToken = jsonNode.has("id_token") ? jsonNode.get("id_token").asText() : null;
            int expiresIn = jsonNode.has("expires_in") ? jsonNode.get("expires_in").asInt() : 3600;
            int refreshExpiresIn = jsonNode.has("refresh_expires_in") ? jsonNode.get("refresh_expires_in").asInt()
                : 1800;
            String tokenType = jsonNode.has("token_type") ? jsonNode.get("token_type").asText() : "Bearer";
            String scope = jsonNode.has("scope") ? jsonNode.get("scope").asText() : "openid profile email";

            return Mono.just(com.winnguyen1905.auth.core.model.response.AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .idToken(idToken)
                .expiresIn(expiresIn)
                .refreshExpiresIn(refreshExpiresIn)
                .tokenType(tokenType)
                .scope(scope)
                .build());
          } catch (Exception e) {
            log.error("Error parsing token refresh response", e);
            return Mono.error(new BaseException("Error parsing token refresh response", 500));
          }
        });
  }

  @Override
  public Mono<Void> logoutFromKeycloak(String refreshToken) {
    log.info("Logging out from Keycloak");

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("client_id", clientId);
    formData.add("client_secret", clientSecret);
    formData.add("refresh_token", refreshToken);

    return getWebClient()
        .post()
        .uri("/realms/{realm}/protocol/openid-connect/logout", realm)
        .body(BodyInserters.fromFormData(formData))
        .retrieve()
        .bodyToMono(Void.class)
        .doOnSuccess(v -> log.info("Successfully logged out from Keycloak"))
        .doOnError(error -> log.error("Failed to logout from Keycloak", error));
  }

  @Override
  public String getAuthorizationUrl(String redirectUri) {
    return UriComponentsBuilder.fromHttpUrl(keycloakServerUrl)
        .path("/realms/{realm}/protocol/openid-connect/auth")
        .queryParam("client_id", clientId)
        .queryParam("redirect_uri", redirectUri)
        .queryParam("response_type", "code")
        .queryParam("scope", "openid profile email")
        .buildAndExpand(realm)
        .toUriString();
  }

  @Override
  public Mono<TokenPair> exchangeCodeForTokens(String code, String redirectUri) {
    log.info("Exchanging authorization code for tokens");

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "authorization_code");
    formData.add("client_id", clientId);
    formData.add("client_secret", clientSecret);
    formData.add("code", code);
    formData.add("redirect_uri", redirectUri);

    return getWebClient()
        .post()
        .uri("/realms/{realm}/protocol/openid-connect/token", realm)
        .body(BodyInserters.fromFormData(formData))
        .retrieve()
        .onStatus(status -> !status.is2xxSuccessful(),
            clientResponse -> clientResponse.bodyToMono(String.class)
                .flatMap(errorBody -> {
                  log.error("Code exchange failed: {}", errorBody);
                  return Mono.error(new BaseException("Authorization code exchange failed", 400));
                }))
        .bodyToMono(String.class)
        .flatMap(response -> {
          try {
            JsonNode jsonNode = objectMapper.readTree(response);
            String accessToken = jsonNode.get("access_token").asText();
            String refreshToken = jsonNode.get("refresh_token").asText();

            return Mono.just(TokenPair.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
          } catch (Exception e) {
            log.error("Error parsing code exchange response", e);
            return Mono.error(new BaseException("Error parsing authorization response", 500));
          }
        });
  }

  public Mono<com.winnguyen1905.auth.core.model.response.AuthResponse> exchangeCodeForTokensFullResponse(String code,
      String redirectUri) {
    log.info("Exchanging authorization code for tokens (full response)");

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "authorization_code");
    formData.add("client_id", clientId);
    formData.add("client_secret", clientSecret);
    formData.add("code", code);
    formData.add("redirect_uri", redirectUri);

    return getWebClient()
        .post()
        .uri("/realms/{realm}/protocol/openid-connect/token", realm)
        .body(BodyInserters.fromFormData(formData))
        .retrieve()
        .onStatus(status -> !status.is2xxSuccessful(),
            clientResponse -> clientResponse.bodyToMono(String.class)
                .flatMap(errorBody -> {
                  log.error("Code exchange failed: {}", errorBody);
                  return Mono.error(new BaseException("Authorization code exchange failed", 400));
                }))
        .bodyToMono(String.class)
        .flatMap(response -> {
          try {
            JsonNode jsonNode = objectMapper.readTree(response);
            String accessToken = jsonNode.get("access_token").asText();
            String refreshToken = jsonNode.get("refresh_token").asText();
            String idToken = jsonNode.has("id_token") ? jsonNode.get("id_token").asText() : null;
            int expiresIn = jsonNode.has("expires_in") ? jsonNode.get("expires_in").asInt() : 3600;
            int refreshExpiresIn = jsonNode.has("refresh_expires_in") ? jsonNode.get("refresh_expires_in").asInt()
                : 1800;
            String tokenType = jsonNode.has("token_type") ? jsonNode.get("token_type").asText() : "Bearer";
            String scope = jsonNode.has("scope") ? jsonNode.get("scope").asText() : "openid profile email";

            return Mono.just(com.winnguyen1905.auth.core.model.response.AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .idToken(idToken)
                .expiresIn(expiresIn)
                .refreshExpiresIn(refreshExpiresIn)
                .tokenType(tokenType)
                .scope(scope)
                .build());
          } catch (Exception e) {
            log.error("Error parsing code exchange response", e);
            return Mono.error(new BaseException("Error parsing authorization response", 500));
          }
        });
  }

  private Mono<String> getAdminAccessToken() {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "password");
    formData.add("client_id", "admin-cli");
    formData.add("username", adminUsername);
    formData.add("password", adminPassword);

    return getWebClient()
        .post()
        .uri("/realms/master/protocol/openid-connect/token")
        .body(BodyInserters.fromFormData(formData))
        .retrieve()
        .bodyToMono(String.class)
        .flatMap(response -> {
          try {
            JsonNode jsonNode = objectMapper.readTree(response);
            return Mono.just(jsonNode.get("access_token").asText());
          } catch (Exception e) {
            log.error("Error parsing admin token response", e);
            return Mono.error(new BaseException("Failed to get admin access token", 500));
          }
        });
  }

  private Map<String, Object> createUserRepresentation(RegisterRequest registerRequest) {
    Map<String, Object> user = new HashMap<>();
    user.put("username", registerRequest.username());
    user.put("email", registerRequest.email());
    user.put("firstName", registerRequest.fullName().split(" ")[0]);
    if (registerRequest.fullName().contains(" ")) {
      user.put("lastName", registerRequest.fullName().substring(registerRequest.fullName().indexOf(" ") + 1));
    }
    user.put("enabled", true);
    user.put("emailVerified", false);

    // Set initial password
    Map<String, Object> credential = new HashMap<>();
    credential.put("type", "password");
    credential.put("value", registerRequest.password());
    credential.put("temporary", false);
    user.put("credentials", new Object[] { credential });

    // Add custom attributes
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("phone", registerRequest.phone());
    attributes.put("address", registerRequest.address());
    attributes.put("accountType", registerRequest.accountType().name());
    user.put("attributes", attributes);

    return user;
  }
}
