package com.winnguyen1905.gateway.persistance.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.winnguyen1905.gateway.persistance.entity.ERole;

@Repository
public interface RoleRepository extends JpaRepository<ERole, UUID> {
    void deleteByIdIn(List<UUID> ids);
    Optional<ERole> findByCode(String code);
}