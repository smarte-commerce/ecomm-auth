package com.winnguyen1905.auth.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.winnguyen1905.auth.common.annotation.ResponseMessage;
import com.winnguyen1905.auth.core.model.response.PagedResponse;
import com.winnguyen1905.auth.core.model.response.PermissionVm;
import com.winnguyen1905.auth.core.model.response.RoleVm;
import com.winnguyen1905.auth.core.service.RoleServiceInterface;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("roles")
public class RoleController {
  
  private final RoleServiceInterface roleService;

  @PostMapping
  @ResponseMessage(message = "Create role success")
  public Mono<ResponseEntity<RoleVm>> createRole(@RequestBody RoleVm role) {
    return this.roleService.createRole(role)
        .map(createdRole -> ResponseEntity.status(HttpStatus.CREATED).body(createdRole));
  }

  @GetMapping("/{id}")
  @ResponseMessage(message = "Get role by id success")
  public Mono<ResponseEntity<RoleVm>> getRoleById(@PathVariable UUID id) {
    return this.roleService.getRoleById(id)
        .map(role -> ResponseEntity.ok(role));
  }
  
  @GetMapping
  @ResponseMessage(message = "Get all roles success")
  public Mono<ResponseEntity<PagedResponse<RoleVm>>> getAllRoles(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "description") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDir) {
    
    return this.roleService.getAllRoles(page, size, sortBy, sortDir)
        .map(response -> ResponseEntity.ok(response));
  }
  
  @GetMapping("/search")
  @ResponseMessage(message = "Search roles success")
  public Mono<ResponseEntity<PagedResponse<RoleVm>>> searchRoles(
      @RequestParam String keyword,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    
    return this.roleService.searchRoles(keyword, page, size)
        .map(response -> ResponseEntity.ok(response));
  }

  @PutMapping("/{id}")
  @ResponseMessage(message = "Update role success")
  public Mono<ResponseEntity<RoleVm>> updateRole(
      @PathVariable UUID id,
      @RequestBody RoleVm role) {
    
    return this.roleService.updateRole(role)
        .map(updatedRole -> ResponseEntity.ok(updatedRole));
  }

  @DeleteMapping("/{ids}")
  @ResponseMessage(message = "Delete roles success")
  public Mono<ResponseEntity<Void>> deleteRoles(@PathVariable List<UUID> ids) {
    return this.roleService.deleteRoles(ids)
        .thenReturn(ResponseEntity.noContent().build());
  }
  
  @GetMapping("/{id}/permissions")
  @ResponseMessage(message = "Get permissions by role id success")
  public Mono<ResponseEntity<List<PermissionVm>>> getPermissionsByRoleId(@PathVariable UUID id) {
    return this.roleService.getPermissionsByRoleId(id)
        .map(permissions -> ResponseEntity.ok(permissions));
  }
  
  @PostMapping("/{roleId}/permissions/{permissionId}")
  @ResponseMessage(message = "Assign permission to role success")
  public Mono<ResponseEntity<RoleVm>> assignPermissionToRole(
      @PathVariable UUID roleId,
      @PathVariable UUID permissionId) {
    
    return this.roleService.assignPermissionToRole(roleId, permissionId)
        .map(role -> ResponseEntity.ok(role));
  }
  
  @DeleteMapping("/{roleId}/permissions/{permissionId}")
  @ResponseMessage(message = "Remove permission from role success")
  public Mono<ResponseEntity<RoleVm>> removePermissionFromRole(
      @PathVariable UUID roleId,
      @PathVariable UUID permissionId) {
    
    return this.roleService.removePermissionFromRole(roleId, permissionId)
        .map(role -> ResponseEntity.ok(role));
  }
  
  @PostMapping("/{roleId}/permissions")
  @ResponseMessage(message = "Assign permissions to role success")
  public Mono<ResponseEntity<RoleVm>> assignPermissionsToRole(
      @PathVariable UUID roleId,
      @RequestBody List<UUID> permissionIds) {
    
    return this.roleService.assignPermissionsToRole(roleId, permissionIds)
        .map(role -> ResponseEntity.ok(role));
  }
}
