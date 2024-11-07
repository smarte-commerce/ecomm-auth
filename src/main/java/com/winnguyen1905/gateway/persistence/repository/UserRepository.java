package com.winnguyen1905.gateway.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.winnguyen1905.gateway.persistence.entity.EUser;

@Repository
public interface UserRepository extends JpaRepository<EUser, UUID> {
    Optional<EUser> findUserByUsername(String username);
    Optional<EUser> findByIdOrUsername(UUID id, String username);
    Optional<EUser> findByUsernameAndRefreshToken(String username, String refreshToken);
}