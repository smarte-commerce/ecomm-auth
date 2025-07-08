package com.winnguyen1905.auth.persistance.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.winnguyen1905.auth.persistance.entity.EPermission;

@Repository
public interface PermissionRepository extends JpaRepository<EPermission, UUID> {
  void deleteByIdIn(List<UUID> ids);
  
  Page<EPermission> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
  
  List<EPermission> findByModuleIgnoreCase(String module);
  
  List<EPermission> findByLeftGreaterThanAndRightLessThan(Integer left, Integer right);
  
  @Modifying
  @Query("UPDATE EPermission p SET p.right = p.right + 2 WHERE p.right >= :value")
  void updateRightValuesForInsert(@Param("value") int value);
  
  @Modifying
  @Query("UPDATE EPermission p SET p.left = p.left + 2 WHERE p.left >= :value")
  void updateLeftValuesForInsert(@Param("value") int value);
}
