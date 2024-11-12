package com.winnguyen1905.gateway.core.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.winnguyen1905.gateway.core.model.Permission;
import com.winnguyen1905.gateway.core.model.request.SearchPermissionRequest;
import com.winnguyen1905.gateway.core.model.response.PagedResponse;
 
public interface IPermissionService {

    Permission handleGetPermissionById(UUID id);

    Permission handleCreatePermission(Permission permission);

    Permission handleUpdatePermission(Permission permissionDTO);

    void handleDeletePermission(List<UUID> ids);

    PagedResponse<Permission> handleGetPermissions(SearchPermissionRequest permissionSearchRequest, Pageable pageable);
}
