package com.winnguyen1905.auth.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.winnguyen1905.auth.common.annotation.ResponseMessage;
import com.winnguyen1905.auth.core.model.response.PagedResponse;
import com.winnguyen1905.auth.core.model.response.PermissionVm;
import com.winnguyen1905.auth.core.service.PermissionServiceInterface;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("permissions")
public class PermissionController {
  
  private final PermissionServiceInterface permissionService;

  @GetMapping("/{id}")
  @ResponseMessage(message = "Get permission by id success")
  public Mono<ResponseEntity<PermissionVm>> getPermissionById(@PathVariable UUID id) {
    return this.permissionService.getPermissionById(id)
        .map(permission -> ResponseEntity.ok(permission));
  }
  
  @GetMapping
  @ResponseMessage(message = "Get all permissions success")
  public Mono<ResponseEntity<PagedResponse<PermissionVm>>> getAllPermissions(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "name") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDir) {
    
    return this.permissionService.getAllPermissions(page, size, sortBy, sortDir)
        .map(response -> ResponseEntity.ok(response));
  }
  
  @GetMapping("/search")
  @ResponseMessage(message = "Search permissions success")
  public Mono<ResponseEntity<PagedResponse<PermissionVm>>> searchPermissions(
      @RequestParam String keyword,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    
    return this.permissionService.searchPermissions(keyword, page, size)
        .map(response -> ResponseEntity.ok(response));
  }
  
  @GetMapping("/module/{module}")
  @ResponseMessage(message = "Get permissions by module success")
  public Mono<ResponseEntity<List<PermissionVm>>> getPermissionsByModule(@PathVariable String module) {
    return this.permissionService.getPermissionsByModule(module)
        .map(permissions -> ResponseEntity.ok(permissions));
  }
  
  @GetMapping("/role/{roleId}")
  @ResponseMessage(message = "Get permissions by role id success")
  public Mono<ResponseEntity<List<PermissionVm>>> getPermissionsByRoleId(@PathVariable UUID roleId) {
    return this.permissionService.getPermissionsByRoleId(roleId)
        .map(permissions -> ResponseEntity.ok(permissions));
  }

  @PostMapping
  @ResponseMessage(message = "Create permission success")
  public Mono<ResponseEntity<PermissionVm>> createPermission(@RequestBody PermissionVm permission) {
    return this.permissionService.createPermission(permission)
        .map(createdPermission -> ResponseEntity.status(HttpStatus.CREATED).body(createdPermission));
  }

  @PutMapping("/{id}")
  @ResponseMessage(message = "Update permission success")
  public Mono<ResponseEntity<PermissionVm>> updatePermission(
      @PathVariable UUID id,
      @RequestBody PermissionVm permission) {
    
    return this.permissionService.updatePermission(permission)
        .map(updatedPermission -> ResponseEntity.ok(updatedPermission));
  }

  @DeleteMapping("/{ids}")
  @ResponseMessage(message = "Delete permissions success")
  public Mono<ResponseEntity<Void>> deletePermission(@PathVariable List<UUID> ids) {
    return this.permissionService.deletePermission(ids)
        .thenReturn(ResponseEntity.noContent().build());
  }
  
  @GetMapping("/{id}/children")
  @ResponseMessage(message = "Get child permissions success")
  public Mono<ResponseEntity<List<PermissionVm>>> getChildPermissions(@PathVariable UUID id) {
    return this.permissionService.getChildPermissions(id)
        .map(permissions -> ResponseEntity.ok(permissions));
  }
  
  @PostMapping("/{childId}/parent/{parentId}")
  @ResponseMessage(message = "Assign parent permission success")
  public Mono<ResponseEntity<PermissionVm>> assignParentPermission(
      @PathVariable UUID childId,
      @PathVariable UUID parentId) {
    
    return this.permissionService.assignParentPermission(childId, parentId)
        .map(permission -> ResponseEntity.ok(permission));
  }
}
