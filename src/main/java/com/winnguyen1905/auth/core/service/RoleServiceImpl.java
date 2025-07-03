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
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;
  private final ModelMapper modelMapper;
  private final PermissionRepository permissionRepository;

  @Override
  public Mono<RoleVm> getRoleById(UUID id) {
    return Mono.fromCallable(() -> this.roleRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found role with id " + id.toString())))
        .map(role -> this.modelMapper.map(role, RoleVm.class));
  }

  @Override
  public Mono<RoleVm> createRole(RoleVm roleDTO) {
    return Mono.fromCallable(() -> {
      ERole role = this.modelMapper.map(roleDTO, ERole.class);
      return this.roleRepository.save(role);
    }).map(role -> this.modelMapper.map(role, RoleVm.class));
  }

  @Override
  public Mono<RoleVm> updateRole(RoleVm roleDTO) {
    return Mono.fromCallable(() -> {
      ERole beModifiedRole = this.roleRepository.findById(roleDTO.id())
          .orElseThrow(() -> new ResourceNotFoundException("Not found role with id " + roleDTO.id().toString()));
      ERole newRoleData = this.modelMapper.map(roleDTO, ERole.class);
      MergeUtils.mergeObject(newRoleData, beModifiedRole);
      return this.roleRepository.save(beModifiedRole);
    }).map(beModifiedRole -> this.modelMapper.map(beModifiedRole, RoleVm.class));
  }

  @Override
  public Mono<Void> deleteRoles(List<UUID> ids) {
    return Mono.fromRunnable(() -> this.roleRepository.deleteByIdIn(ids)).then();
  }
}
