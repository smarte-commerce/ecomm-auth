package com.winnguyen1905.auth.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winnguyen1905.auth.core.model.response.PagedResponse;
import com.winnguyen1905.auth.core.model.response.PermissionVm;
import com.winnguyen1905.auth.core.model.response.RoleVm;
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
public class RoleServiceImpl implements RoleServiceInterface {

  private final RoleRepository roleRepository;
  private final ModelMapper modelMapper;
  private final PermissionRepository permissionRepository;

  @Override
  public Mono<RoleVm> getRoleById(UUID id) {
    return Mono.fromCallable(() -> this.roleRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found role with id " + id.toString())))
        .map(role -> this.modelMapper.map(role, RoleVm.class))
        .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<RoleVm> createRole(RoleVm roleDTO) {
    return Mono.fromCallable(() -> {
      ERole role = this.modelMapper.map(roleDTO, ERole.class);
      return this.roleRepository.save(role);
    })
    .map(role -> this.modelMapper.map(role, RoleVm.class))
    .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<RoleVm> updateRole(RoleVm roleDTO) {
    return Mono.fromCallable(() -> {
      ERole beModifiedRole = this.roleRepository.findById(roleDTO.id())
          .orElseThrow(() -> new ResourceNotFoundException("Not found role with id " + roleDTO.id().toString()));
      ERole newRoleData = this.modelMapper.map(roleDTO, ERole.class);
      MergeUtils.mergeObject(newRoleData, beModifiedRole);
      return this.roleRepository.save(beModifiedRole);
    })
    .map(beModifiedRole -> this.modelMapper.map(beModifiedRole, RoleVm.class))
    .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<Void> deleteRoles(List<UUID> ids) {
    return Mono.fromRunnable(() -> this.roleRepository.deleteByIdIn(ids))
        .subscribeOn(Schedulers.boundedElastic())
        .then();
  }
  
  @Override
  public Mono<PagedResponse<RoleVm>> getAllRoles(int page, int size, String sortBy, String sortDir) {
    return Mono.fromCallable(() -> {
      Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) 
        ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();
      
      Pageable pageable = PageRequest.of(page, size, sort);
      Page<ERole> rolesPage = this.roleRepository.findAll(pageable);
      
      List<RoleVm> content = rolesPage.getContent().stream()
        .map(role -> this.modelMapper.map(role, RoleVm.class))
        .collect(Collectors.toList());
      
      return new PagedResponse<>(
        content,
        rolesPage.getNumber(),
        rolesPage.getSize(),
        rolesPage.getTotalElements(),
        rolesPage.getTotalPages(),
        rolesPage.isLast()
      );
    })
    .subscribeOn(Schedulers.boundedElastic());
  }
  
  @Override
  public Mono<PagedResponse<RoleVm>> searchRoles(String keyword, int page, int size) {
    return Mono.fromCallable(() -> {
      Pageable pageable = PageRequest.of(page, size);
      Page<ERole> rolesPage = this.roleRepository.findByDescriptionContainingIgnoreCase(keyword, pageable);
      
      List<RoleVm> content = rolesPage.getContent().stream()
        .map(role -> this.modelMapper.map(role, RoleVm.class))
        .collect(Collectors.toList());
      
      return new PagedResponse<>(
        content,
        rolesPage.getNumber(),
        rolesPage.getSize(),
        rolesPage.getTotalElements(),
        rolesPage.getTotalPages(),
        rolesPage.isLast()
      );
    })
    .subscribeOn(Schedulers.boundedElastic());
  }
  
  @Override
  @Transactional
  public Mono<RoleVm> assignPermissionToRole(UUID roleId, UUID permissionId) {
    return Mono.fromCallable(() -> {
      ERole role = this.roleRepository.findById(roleId)
          .orElseThrow(() -> new ResourceNotFoundException("Not found role with id " + roleId));
      
      EPermission permission = this.permissionRepository.findById(permissionId)
          .orElseThrow(() -> new ResourceNotFoundException("Not found permission with id " + permissionId));
      
      if (role.getPermissions() == null) {
        role.setPermissions(new java.util.HashSet<>());
      }
      
      role.getPermissions().add(permission);
      permission.getRoles().add(role);
      
      return this.roleRepository.save(role);
    })
    .map(role -> this.modelMapper.map(role, RoleVm.class))
    .subscribeOn(Schedulers.boundedElastic());
  }
  
  @Override
  @Transactional
  public Mono<RoleVm> removePermissionFromRole(UUID roleId, UUID permissionId) {
    return Mono.fromCallable(() -> {
      ERole role = this.roleRepository.findById(roleId)
          .orElseThrow(() -> new ResourceNotFoundException("Not found role with id " + roleId));
      
      EPermission permission = this.permissionRepository.findById(permissionId)
          .orElseThrow(() -> new ResourceNotFoundException("Not found permission with id " + permissionId));
      
      if (role.getPermissions() != null) {
        role.getPermissions().remove(permission);
        permission.getRoles().remove(role);
      }
      
      return this.roleRepository.save(role);
    })
    .map(role -> this.modelMapper.map(role, RoleVm.class))
    .subscribeOn(Schedulers.boundedElastic());
  }
  
  @Override
  @Transactional
  public Mono<RoleVm> assignPermissionsToRole(UUID roleId, List<UUID> permissionIds) {
    return Mono.fromCallable(() -> {
      ERole role = this.roleRepository.findById(roleId)
          .orElseThrow(() -> new ResourceNotFoundException("Not found role with id " + roleId));
      
      List<EPermission> permissions = this.permissionRepository.findAllById(permissionIds);
      
      if (permissions.size() != permissionIds.size()) {
        throw new ResourceNotFoundException("One or more permissions not found");
      }
      
      if (role.getPermissions() == null) {
        role.setPermissions(new java.util.HashSet<>());
      }
      
      for (EPermission permission : permissions) {
        role.getPermissions().add(permission);
        permission.getRoles().add(role);
      }
      
      return this.roleRepository.save(role);
    })
    .map(role -> this.modelMapper.map(role, RoleVm.class))
    .subscribeOn(Schedulers.boundedElastic());
  }
  
  @Override
  public Mono<List<PermissionVm>> getPermissionsByRoleId(UUID roleId) {
    return Mono.<List<PermissionVm>>fromCallable(() -> {
      ERole role = this.roleRepository.findById(roleId)
          .orElseThrow(() -> new ResourceNotFoundException("Not found role with id " + roleId));
      
      if (role.getPermissions() == null) {
        return new ArrayList<>();
      }
      
      return new ArrayList<>(role.getPermissions()).stream()
          .map(permission -> this.modelMapper.map(permission, PermissionVm.class))
          .collect(Collectors.toList());
    })
    .subscribeOn(Schedulers.boundedElastic());
  }
}
