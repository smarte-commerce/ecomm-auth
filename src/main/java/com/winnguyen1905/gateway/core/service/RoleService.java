package com.winnguyen1905.gateway.core.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.winnguyen1905.gateway.core.model.Role;
import com.winnguyen1905.gateway.exception.ResourceNotFoundException;
import com.winnguyen1905.gateway.persistance.entity.EPermission;
import com.winnguyen1905.gateway.persistance.entity.ERole;
import com.winnguyen1905.gateway.persistance.repository.PermissionRepository;
import com.winnguyen1905.gateway.persistance.repository.RoleRepository;
import com.winnguyen1905.gateway.util.MergeUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

  private final RoleRepository roleRepository;
  private final ModelMapper modelMapper;
  private final PermissionRepository permissionRepository;

  @Override
  public Role handleGetRoleById(UUID id) {
    ERole role = this.roleRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found role with id " + id.toString()));
    return this.modelMapper.map(role, Role.class);
  }

  @Override
  public Role handleCreateRole(Role roleDTO) {
    ERole role = this.modelMapper.map(roleDTO, ERole.class);
    List<EPermission> permissions = this.permissionRepository.findByCodeIn(roleDTO.getPermissionCodes());
    role.setPermissions(Set.copyOf(permissions));
    role = this.roleRepository.save(role);
    return this.modelMapper.map(role, Role.class);
  }

  @Override
  public Role handleUpdateRole(Role roleDTO) {
    ERole beModifiedRole = this.roleRepository.findById(roleDTO.getId())
        .orElseThrow(() -> new ResourceNotFoundException("Not found role with id " + roleDTO.getId().toString()));
    ERole newRoleData = this.modelMapper.map(roleDTO, ERole.class);
    MergeUtils.mergeObject(newRoleData, beModifiedRole);
    beModifiedRole = this.roleRepository.save(beModifiedRole);
    return this.modelMapper.map(beModifiedRole, Role.class);
  }

  @Override
  public void handleDeleteRoles(List<UUID> ids) {
    this.roleRepository.deleteByIdIn(ids);
  }
}
