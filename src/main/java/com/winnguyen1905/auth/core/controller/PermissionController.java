package com.winnguyen1905.auth.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winnguyen1905.auth.common.annotation.AccountRequest;
import com.winnguyen1905.auth.common.annotation.TAccountRequest;
import com.winnguyen1905.auth.common.constant.SystemConstant;
import com.winnguyen1905.auth.core.model.response.PagedResponse;
import com.winnguyen1905.auth.core.model.response.PermissionVm;
import com.winnguyen1905.auth.core.service.IPermissionService;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("permissions")
public class PermissionController {
  // @Autowired
  // private IPermissionService permissionService;

  @GetMapping("/path")
  public String getMethodName(@AccountRequest TAccountRequest param) {
      return new String();
  }
  

  // @GetMapping
  // public ResponseEntity<PagedResponse<Permission>> getPermissions(
  //     @ModelAttribute(SystemConstant.MODEL) SearchPermissionRequest permissionSearchRequest,
  //     Pageable pageable) {
  //   return ResponseEntity.ok().body(
  //       this.permissionService.handleGetPermissions(permissionSearchRequest, pageable));
  // }

  // @PostMapping
  // public ResponseEntity<Permission> createPermission(
  //     @RequestBody Permission permission) throws Exception {
  //   return ResponseEntity.status(HttpStatus.CREATED).body(
  //       this.permissionService.handleCreatePermission(permission));
  // }

  // @PutMapping
  // public ResponseEntity<Permission> updatePermission(@RequestBody Permission permission) {
  //   return ResponseEntity.ok().body(this.permissionService.handleUpdatePermission(permission));
  // }

  // @DeleteMapping("/{ids}")
  // public ResponseEntity<Permission> deletePermission(
  //     @PathVariable List<UUID> ids) {
  //   this.permissionService.handleDeletePermission(ids);
  //   return ResponseEntity.noContent().build();
  // }
}
