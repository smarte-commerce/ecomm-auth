package com.winnguyen1905.gateway.persistance.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.winnguyen1905.gateway.persistance.entity.EUserCredentials;

import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<EUserCredentials, UUID> {
  Mono<EUserCredentials> findUserByUsername(String username);

  Mono<EUserCredentials> findByIdOrUsername(UUID id, String username);

  Mono<EUserCredentials> findByUsernameAndRefreshToken(String username, String refreshToken);
}
