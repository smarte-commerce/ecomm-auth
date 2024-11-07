package com.winnguyen1905.gateway.config;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.server.BearerTokenServerAuthenticationEntryPoint;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winnguyen1905.gateway.model.response.RestResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomServerAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    private final ServerAuthenticationEntryPoint delegate = new BearerTokenServerAuthenticationEntryPoint();

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException authException) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        this.delegate.commence(exchange, authException);

        RestResponse<Object> res = RestResponse.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message("Authentication failed, please check your token")
                .error(
                        Optional.ofNullable(authException.getCause())
                                .map(Throwable::getMessage)
                                .orElse(authException.getMessage()))
                .build();

        return exchange.getResponse().writeWith(
                Mono.fromSupplier(() -> {
                    try {
                        return exchange.getResponse()
                                .bufferFactory()
                                .wrap(objectMapper.writeValueAsBytes(res));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Error writing authentication error response", e);
                    }
                }));
    }
}