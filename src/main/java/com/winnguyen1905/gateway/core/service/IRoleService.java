package com.winnguyen1905.gateway.core.service;

import java.util.List;
import java.util.UUID;

import com.winnguyen1905.gateway.model.RoleDTO;

public interface IRoleService {
    // RoleDTO handleGetRoles(RoleSearchRequest RoleSearchRequest, Pageable
    // pageable);

    RoleDTO handleGetRoleById(UUID id);

    RoleDTO handleCreateRole(RoleDTO roleDTO);

    RoleDTO handleUpdateRole(RoleDTO roleDTO);

    void handleDeleteRoles(List<UUID> ids);
}