package com.winnguyen1905.gateway.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; 

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


@RestController
@RequestMapping("${release.api.prefix}/permissions")
public class PermissionController {
    @Autowired
    private IPermissionService permissionService;

    @GetMapping
    public ResponseEntity<PermissionDTO> getPermissions(
        @ModelAttribute(SystemConstant.MODEL) SearchPermissionRequest permissionSearchRequest,
        Pageable pageable
    ) {
        return ResponseEntity.ok().body(
            this.permissionService.handleGetPermissions(permissionSearchRequest, pageable)
        );
    }

    @PostMapping
    public ResponseEntity<PermissionDTO> createPermission(
        @RequestBody PermissionDTO permissionDTO
    ) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            this.permissionService.handleCreatePermission(permissionDTO)
        );
    }   
    
    @PutMapping
    public ResponseEntity<PermissionDTO> updatePermission(@RequestBody PermissionDTO permissionDTO) {
        return ResponseEntity.ok().body(this.permissionService.handleUpdatePermission(permissionDTO));
    }
    
    @DeleteMapping("/{ids}")
    public ResponseEntity<PermissionDTO> deletePermission(
        @PathVariable List<UUID> ids
    ) {
        this.permissionService.handleDeletePermission(ids);
        return ResponseEntity.noContent().build();
    }
}
