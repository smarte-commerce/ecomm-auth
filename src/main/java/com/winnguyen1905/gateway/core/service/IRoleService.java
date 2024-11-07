package com.winnguyen1905.gateway.core.service;

import java.util.List;
import java.util.UUID;

import com.winnguyen1905.gateway.core.model.Role;

public interface IRoleService {
    // Role handleGetRoles(RoleSearchRequest RoleSearchRequest, Pageable
    // pageable);
    Role handleGetRoleById(UUID id);

    Role handleCreateRole(Role roleDTO);

    Role handleUpdateRole(Role roleDTO);

    void handleDeleteRoles(List<UUID> ids);
}