package com.winnguyen1905.auth.persistance.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.winnguyen1905.auth.persistance.entity.EPermission;

// @Repository
public interface PermissionRepository  {
  // void deleteByIdIn(List<UUID> ids);
  // List<EPermission> findByCodeIn(List<String> code);
}
