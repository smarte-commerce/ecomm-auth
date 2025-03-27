package com.winnguyen1905.auth.core.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.winnguyen1905.auth.core.model.response.PagedResponse;
import com.winnguyen1905.auth.core.model.response.PermissionVm;

public interface IPermissionService {
  PermissionVm handleGetPermissionById(UUID id);

  void handleDeletePermission(List<UUID> ids);

  PermissionVm handleCreatePermission(PermissionVm permission);

  PermissionVm handleUpdatePermission(PermissionVm permissionDTO);

}
