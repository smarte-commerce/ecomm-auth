package com.winnguyen1905.auth.persistance.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.winnguyen1905.auth.persistance.entity.EAccountCredentials;

public interface UserRepository extends JpaRepository<EAccountCredentials, UUID> {
  Optional<EAccountCredentials> findUserByUsername(String username);

  Optional<EAccountCredentials> findByIdOrUsername(UUID id, String username);

  Optional<EAccountCredentials> findByUsernameAndRefreshToken(String username, String refreshToken);
}
