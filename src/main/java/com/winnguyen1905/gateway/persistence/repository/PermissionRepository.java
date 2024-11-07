package com.winnguyen1905.gateway.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.winnguyen1905.gateway.persistence.entity.EPermission;

@Repository
public interface PermissionRepository extends JpaRepository<EPermission, UUID>, JpaSpecificationExecutor<EPermission> {
    void deleteByIdIn(List<UUID> ids);
    List<EPermission> findByCodeIn(List<String> code);
}