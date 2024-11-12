package com.winnguyen1905.gateway.persistance.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.winnguyen1905.gateway.persistance.entity.EUserCredentials;

@Repository
public interface UserRepository extends JpaRepository<EUserCredentials, UUID> {
  Optional<EUserCredentials> findUserByUsername(String username);

  Optional<EUserCredentials> findByIdOrUsername(UUID id, String username);

  Optional<EUserCredentials> findByUsernameAndRefreshToken(String username, String refreshToken);
}
