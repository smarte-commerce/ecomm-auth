package com.winnguyen1905.auth.core.service;

import java.util.List;
import java.util.UUID;

import com.winnguyen1905.auth.core.model.response.RoleVm;

public interface IRoleService {
    // Role handleGetRoles(RoleSearchRequest RoleSearchRequest, Pageable
    // pageable);
    RoleVm handleGetRoleById(UUID id);

    RoleVm handleCreateRole(RoleVm roleDTO);

    RoleVm handleUpdateRole(RoleVm roleDTO);

    void handleDeleteRoles(List<UUID> ids);
}
