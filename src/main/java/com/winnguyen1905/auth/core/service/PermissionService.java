package com.winnguyen1905.auth.core.service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.winnguyen1905.auth.core.model.response.PagedResponse;
import com.winnguyen1905.auth.core.model.response.PermissionVm;
import com.winnguyen1905.auth.exception.ResourceNotFoundException;
import com.winnguyen1905.auth.persistance.entity.EPermission;
import com.winnguyen1905.auth.persistance.repository.PermissionRepository;
import com.winnguyen1905.auth.util.MergeUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionService {

  // private final ModelMapper mapper;
  // private final PermissionConverter permissionConverter;
  // private final PermissionRepository permissionRepository;
  // private final Type pagedResponseType = new TypeToken<PagedResponse<Permission>>() {
  // }.getType();

  // @Override
  // public PagedResponse<Permission> handleGetPermissions(SearchPermissionRequest permissionSearchRequest,
  //     Pageable pageable) {
  //   Specification<EPermission> spec = this.permissionConverter.toPermissionSpec(permissionSearchRequest);
  //   Page<EPermission> permissions = this.permissionRepository.findAll(spec, pageable);
  //   PagedResponse<Permission> permissionPaged = this.mapper.map(permissions, pagedResponseType);
  //   permissionPaged.setPage((int) permissions.getNumber() + 1);
  //   return permissionPaged;
  // }

  // @Override
  // public Permission handleGetPermissionById(UUID id) {
  //   return null;
  // }

  // @Override
  // public Permission handleCreatePermission(Permission permission) {
  //   EPermission ePermission = this.mapper.map(permission, EPermission.class);
  //   ePermission = this.permissionRepository.save(ePermission);
  //   return this.mapper.map(ePermission, Permission.class);
  // }

  // @Override
  // public Permission handleUpdatePermission(Permission permission) {
  //   EPermission beModifiedPermission = this.permissionRepository.findById(permission.id())
  //       .orElseThrow(() -> new ResourceNotFoundException("Not found permission by id " + permission.id()));
  //   EPermission ePermission = this.mapper.map(permission, EPermission.class);
  //   MergeUtils.mergeObject(ePermission, beModifiedPermission);
  //   beModifiedPermission = this.permissionRepository.save(beModifiedPermission);
  //   return this.mapper.map(beModifiedPermission, Permission.class);
  // }

  // @Override
  // public void handleDeletePermission(List<UUID> ids) {
  //   this.permissionRepository.deleteByIdIn(ids);
  // }
}
