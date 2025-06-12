package com.winnguyen1905.auth.config;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.winnguyen1905.auth.common.annotation.AccountRequest;
import com.winnguyen1905.auth.common.annotation.TAccountRequest;
import com.winnguyen1905.auth.common.constant.AccountType;

import reactor.core.publisher.Mono;

@Component
public class AccountRequestResolver {

  public static enum AccountRequestArgument {
    ID("sub"), USERNAME("username"), ROLE("role");

    String value;

    AccountRequestArgument(String value) {
      this.value = value;
    }
  };

  public Mono<TAccountRequest> resolveCurrentUser(ServerWebExchange exchange) {
    return ReactiveSecurityContextHolder.getContext()
        .map(securityContext -> securityContext.getAuthentication())
        .filter(authentication -> authentication != null && authentication.getPrincipal() instanceof Jwt)
        .map(Authentication::getPrincipal)
        .cast(Jwt.class)
        .map(jwt -> {
          UUID id = UUID.fromString(jwt.getClaimAsString(AccountRequestArgument.ID.value));
          String username = jwt.getClaimAsString(AccountRequestArgument.USERNAME.value);
          AccountType role = AccountType.valueOf(jwt.getClaimAsString(AccountRequestArgument.ROLE.value));
          return TAccountRequest.builder()
              .id(id)
              .username(username)
              .role(role)
              .build();
        })
        .switchIfEmpty(Mono.error(new IllegalStateException("No valid JWT authentication found")));
  }
}
