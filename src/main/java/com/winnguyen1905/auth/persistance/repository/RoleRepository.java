package com.winnguyen1905.auth.persistance.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.winnguyen1905.auth.persistance.entity.ERole;


@Repository
public interface RoleRepository extends JpaRepository<ERole, UUID> {
  Optional<ERole> findByCode(String code);
  Optional<Void> deleteByIdIn(List<UUID> ids);
}
