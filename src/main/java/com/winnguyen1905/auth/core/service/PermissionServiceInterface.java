package com.winnguyen1905.auth.core.service;

import java.util.List;
import java.util.UUID;

import com.winnguyen1905.auth.core.model.response.PagedResponse;
import com.winnguyen1905.auth.core.model.response.PermissionVm;

import reactor.core.publisher.Mono;

public interface PermissionServiceInterface {
    // Basic CRUD operations
    Mono<PermissionVm> getPermissionById(UUID id);
    Mono<PermissionVm> createPermission(PermissionVm permissionVm);
    Mono<PermissionVm> updatePermission(PermissionVm permissionVm);
    Mono<Void> deletePermission(List<UUID> ids);
    
    // Advanced operations
    Mono<PagedResponse<PermissionVm>> getAllPermissions(int page, int size, String sortBy, String sortDir);
    Mono<PagedResponse<PermissionVm>> searchPermissions(String keyword, int page, int size);
    Mono<List<PermissionVm>> getPermissionsByModule(String module);
    Mono<List<PermissionVm>> getPermissionsByRoleId(UUID roleId);
    
    // Hierarchical operations
    Mono<List<PermissionVm>> getChildPermissions(UUID parentId);
    Mono<PermissionVm> assignParentPermission(UUID childId, UUID parentId);
} 
