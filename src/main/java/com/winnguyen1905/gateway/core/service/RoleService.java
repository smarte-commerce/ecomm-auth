package com.winnguyen1905.gateway.core.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.winnguyen1905.gateway.entity.PermissionEntity;
import com.winnguyen1905.gateway.entity.RoleEntity;
import com.winnguyen1905.gateway.exception.CustomRuntimeException;
import com.winnguyen1905.gateway.model.RoleDTO;
import com.winnguyen1905.gateway.repository.PermissionRepository;
import com.winnguyen1905.gateway.repository.RoleRepository;
import com.winnguyen1905.gateway.service.IRoleService;
import com.winnguyen1905.gateway.util.MergeUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PermissionRepository permissionRepository;

    @Override
    public RoleDTO handleGetRoleById(UUID id) {
        RoleEntity role = this.roleRepository.findById(id)
                .orElseThrow(() -> new CustomRuntimeException("Not found role with id " + id.toString()));
        return this.modelMapper.map(role, RoleDTO.class);
    }

    @Override
    public RoleDTO handleCreateRole(RoleDTO roleDTO) {
        RoleEntity role = this.modelMapper.map(roleDTO, RoleEntity.class);
        List<PermissionEntity> permissions = this.permissionRepository.findByCodeIn(roleDTO.getPermissionCodes());
        role.setPermissions(Set.copyOf(permissions));
        role = this.roleRepository.save(role);
        return this.modelMapper.map(role, RoleDTO.class);
    }

    @Override
    public RoleDTO handleUpdateRole(RoleDTO roleDTO) {
        RoleEntity beModifiedRole = this.roleRepository.findById(roleDTO.getId())
                .orElseThrow(() -> new CustomRuntimeException("Not found role with id " + roleDTO.getId().toString()));
        RoleEntity newRoleData = this.modelMapper.map(roleDTO, RoleEntity.class);
        MergeUtils.mergeObject(newRoleData, beModifiedRole);
        beModifiedRole = this.roleRepository.save(beModifiedRole);
        return this.modelMapper.map(beModifiedRole, RoleDTO.class);
    }

    @Override
    public void handleDeleteRoles(List<UUID> ids) {
        this.roleRepository.deleteByIdIn(ids);
    }
}