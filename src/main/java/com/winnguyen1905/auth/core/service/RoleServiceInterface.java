package com.winnguyen1905.auth.core.service;

import java.util.List;
import java.util.UUID;

import com.winnguyen1905.auth.core.model.response.PagedResponse;
import com.winnguyen1905.auth.core.model.response.PermissionVm;
import com.winnguyen1905.auth.core.model.response.RoleVm;

import reactor.core.publisher.Mono;

public interface RoleServiceInterface {
    // Basic CRUD operations
    Mono<RoleVm> getRoleById(UUID id);
    Mono<RoleVm> createRole(RoleVm roleVm);
    Mono<RoleVm> updateRole(RoleVm roleVm);
    Mono<Void> deleteRoles(List<UUID> ids);
    
    // Advanced operations
    Mono<PagedResponse<RoleVm>> getAllRoles(int page, int size, String sortBy, String sortDir);
    Mono<PagedResponse<RoleVm>> searchRoles(String keyword, int page, int size);
    
    // Permission management
    Mono<RoleVm> assignPermissionToRole(UUID roleId, UUID permissionId);
    Mono<RoleVm> removePermissionFromRole(UUID roleId, UUID permissionId);
    Mono<RoleVm> assignPermissionsToRole(UUID roleId, List<UUID> permissionIds);
    Mono<List<PermissionVm>> getPermissionsByRoleId(UUID roleId);
} 
