package com.winnguyen1905.auth.core.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.winnguyen1905.auth.core.model.response.RoleVm;
import com.winnguyen1905.auth.exception.ResourceNotFoundException;
import com.winnguyen1905.auth.persistance.entity.EPermission;
import com.winnguyen1905.auth.persistance.entity.ERole;
import com.winnguyen1905.auth.persistance.repository.PermissionRepository;
import com.winnguyen1905.auth.persistance.repository.RoleRepository;
import com.winnguyen1905.auth.util.MergeUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

  // private final RoleRepository roleRepository;
  // private final ModelMapper modelMapper;
  // private final PermissionRepository permissionRepository;

  // @Override
  // public RoleVm handleGetRoleById(UUID id) {
  //   ERole role = this.roleRepository.findById(id)
  //       .orElseThrow(() -> new ResourceNotFoundException("Not found role with id " + id.toString()));
  //   return this.modelMapper.map(role, RoleVm.class);
  // }

  // @Override
  // public RoleVm handleCreateRole(RoleVm roleDTO) {
  //   ERole role = this.modelMapper.map(roleDTO, ERole.class);
  //   // List<EPermission> permissions = this.permissionRepository.findByCodeIn(roleDTO.permissionCodes());
  //   // role.setPermissions(Set.copyOf(permissions));
  //   role = this.roleRepository.save(role);
  //   return this.modelMapper.map(role, RoleVm.class);
  // }

  // @Override
  // public RoleVm handleUpdateRole(RoleVm roleDTO) {
  //   ERole beModifiedRole = this.roleRepository.findById(roleDTO.id())
  //       .orElseThrow(() -> new ResourceNotFoundException("Not found role with id " + roleDTO.id().toString()));
  //   ERole newRoleData = this.modelMapper.map(roleDTO, ERole.class);
  //   MergeUtils.mergeObject(newRoleData, beModifiedRole);
  //   beModifiedRole = this.roleRepository.save(beModifiedRole);
  //   return this.modelMapper.map(beModifiedRole, RoleVm.class);
  // }

  // @Override
  // public void handleDeleteRoles(List<UUID> ids) {
  //   this.roleRepository.deleteByIdIn(ids);
  // }
}
