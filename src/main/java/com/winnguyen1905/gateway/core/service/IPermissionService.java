package com.winnguyen1905.gateway.core.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.winnguyen1905.gateway.model.PermissionDTO;
import com.winnguyen1905.gateway.model.request.SearchPermissionRequest;
import com.winnguyen1905.gateway.model.response.PaginationResponse;

public interface IPermissionService {
    PaginationResponse<PermissionDTO> handleGetPermissions(SearchPermissionRequest permissionSearchRequest,
            Pageable pageable);

    PermissionDTO handleGetPermissionById(UUID id);

    PermissionDTO handleCreatePermission(PermissionDTO permissionDTO);

    PermissionDTO handleUpdatePermission(PermissionDTO permissionDTO);

    void handleDeletePermission(List<UUID> ids);
}