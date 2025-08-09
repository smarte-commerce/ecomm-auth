package com.winnguyen1905.auth.core.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winnguyen1905.auth.core.model.response.PagedResponse;
import com.winnguyen1905.auth.core.model.response.PermissionVm;
import com.winnguyen1905.auth.core.service.PermissionServiceInterface;
import com.winnguyen1905.auth.exception.ResourceNotFoundException;
import com.winnguyen1905.auth.persistance.entity.EPermission;
import com.winnguyen1905.auth.persistance.entity.ERole;
import com.winnguyen1905.auth.persistance.repository.PermissionRepository;
import com.winnguyen1905.auth.persistance.repository.RoleRepository;
import com.winnguyen1905.auth.util.MergeUtils;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionServiceInterface {

  private final ModelMapper mapper;
  private final PermissionRepository permissionRepository;
  private final RoleRepository roleRepository;

  @Override
  public Mono<PermissionVm> getPermissionById(UUID id) {
    return Mono.fromCallable(() -> 
      this.permissionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found permission by id " + id))
    )
    .map(permission -> this.mapper.map(permission, PermissionVm.class))
    .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<PermissionVm> createPermission(PermissionVm permission) {
    return Mono.fromCallable(() -> {
      EPermission ePermission = this.mapper.map(permission, EPermission.class);
      return this.permissionRepository.save(ePermission);
    })
    .map(ePermission -> this.mapper.map(ePermission, PermissionVm.class))
    .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<PermissionVm> updatePermission(PermissionVm permission) {
    return Mono.fromCallable(() -> {
      EPermission beModifiedPermission = this.permissionRepository.findById(permission.id())
          .orElseThrow(() -> new ResourceNotFoundException("Not found permission by id " + permission.id()));
      EPermission ePermission = this.mapper.map(permission, EPermission.class);
      MergeUtils.mergeObject(ePermission, beModifiedPermission);
      return this.permissionRepository.save(beModifiedPermission);
    })
    .map(beModifiedPermission -> this.mapper.map(beModifiedPermission, PermissionVm.class))
    .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<Void> deletePermission(List<UUID> ids) {
    return Mono.fromRunnable(() -> this.permissionRepository.deleteByIdIn(ids))
      .subscribeOn(Schedulers.boundedElastic())
      .then();
  }

  @Override
  public Mono<PagedResponse<PermissionVm>> getAllPermissions(int page, int size, String sortBy, String sortDir) {
    return Mono.fromCallable(() -> {
      Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) 
        ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();
      
      Pageable pageable = PageRequest.of(page, size, sort);
      Page<EPermission> permissionsPage = this.permissionRepository.findAll(pageable);
      
      List<PermissionVm> content = permissionsPage.getContent().stream()
        .map(permission -> this.mapper.map(permission, PermissionVm.class))
        .collect(Collectors.toList());
      
      return new PagedResponse<>(
        content,
        permissionsPage.getNumber(),
        permissionsPage.getSize(),
        permissionsPage.getTotalElements(),
        permissionsPage.getTotalPages(),
        permissionsPage.isLast()
      );
    })
    .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<PagedResponse<PermissionVm>> searchPermissions(String keyword, int page, int size) {
    return Mono.fromCallable(() -> {
      Pageable pageable = PageRequest.of(page, size);
      Page<EPermission> permissionsPage = this.permissionRepository.findByNameContainingIgnoreCase(keyword, pageable);
      
      List<PermissionVm> content = permissionsPage.getContent().stream()
        .map(permission -> this.mapper.map(permission, PermissionVm.class))
        .collect(Collectors.toList());
      
      return new PagedResponse<>(
        content,
        permissionsPage.getNumber(),
        permissionsPage.getSize(),
        permissionsPage.getTotalElements(),
        permissionsPage.getTotalPages(),
        permissionsPage.isLast()
      );
    })
    .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<List<PermissionVm>> getPermissionsByModule(String module) {
    return Mono.<List<PermissionVm>>fromCallable(() -> {
      List<EPermission> permissions = this.permissionRepository.findByModuleIgnoreCase(module);
      
      return permissions.stream()
        .map(permission -> this.mapper.map(permission, PermissionVm.class))
        .collect(Collectors.toList());
    })
    .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<List<PermissionVm>> getPermissionsByRoleId(UUID roleId) {
    return Mono.<List<PermissionVm>>fromCallable(() -> {
      ERole role = this.roleRepository.findById(roleId)
          .orElseThrow(() -> new ResourceNotFoundException("Not found role with id " + roleId));
      
      return new ArrayList<>(role.getPermissions()).stream()
        .map(permission -> this.mapper.map(permission, PermissionVm.class))
        .collect(Collectors.toList());
    })
    .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<List<PermissionVm>> getChildPermissions(UUID parentId) {
    return Mono.<List<PermissionVm>>fromCallable(() -> {
      EPermission parent = this.permissionRepository.findById(parentId)
          .orElseThrow(() -> new ResourceNotFoundException("Not found permission with id " + parentId));
      
      List<EPermission> children = this.permissionRepository.findByLeftGreaterThanAndRightLessThan(parent.getLeft(), parent.getRight());
      
      return children.stream()
        .map(permission -> this.mapper.map(permission, PermissionVm.class))
        .collect(Collectors.toList());
    })
    .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  @Transactional
  public Mono<PermissionVm> assignParentPermission(UUID childId, UUID parentId) {
    return Mono.fromCallable(() -> {
      EPermission child = this.permissionRepository.findById(childId)
          .orElseThrow(() -> new ResourceNotFoundException("Not found child permission with id " + childId));
      
      EPermission parent = this.permissionRepository.findById(parentId)
          .orElseThrow(() -> new ResourceNotFoundException("Not found parent permission with id " + parentId));
      
      // Implement nested set model logic here
      // This is a simplified implementation and might need adjustment based on your actual nested set implementation
      int right = parent.getRight();
      
      // Update all nodes with right values >= parent's right to create space for the child node
      this.permissionRepository.updateRightValuesForInsert(right);
      this.permissionRepository.updateLeftValuesForInsert(right);
      
      // Place child between parent's right - 1 and parent's right
      child.setLeft(right);
      child.setRight(right + 1);
      
      return this.permissionRepository.save(child);
    })
    .map(child -> this.mapper.map(child, PermissionVm.class))
    .subscribeOn(Schedulers.boundedElastic());
  }
}
