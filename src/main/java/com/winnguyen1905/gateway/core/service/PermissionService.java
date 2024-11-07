package com.winnguyen1905.gateway.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.winnguyen1905.gateway.converter.PermissionConverter;
import com.winnguyen1905.gateway.entity.PermissionEntity;
import com.winnguyen1905.gateway.exception.CustomRuntimeException;
import com.winnguyen1905.gateway.model.BaseObjectDTO;
import com.winnguyen1905.gateway.model.PermissionDTO;
import com.winnguyen1905.gateway.model.request.SearchPermissionRequest;
import com.winnguyen1905.gateway.repository.PermissionRepository;
import com.winnguyen1905.gateway.repository.specification.QuerySpecification;
import com.winnguyen1905.gateway.service.IPermissionService;
import com.winnguyen1905.gateway.util.MergeUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionService implements IPermissionService {

    private final ModelMapper modelMapper;
    private final PermissionConverter permissionConverter;
    private final PermissionRepository permissionRepository;

    @Override
    public PermissionDTO handleGetPermissions(SearchPermissionRequest permissionSearchRequest, Pageable pageable) {
        Specification<PermissionEntity> spec = this.permissionConverter.toPermissionSpec(permissionSearchRequest);
        Page<PermissionEntity> permissions = this.permissionRepository.findAll(spec, pageable);
        PermissionDTO permissionDTOs = this.modelMapper.map(permissions, PermissionDTO.class);
        permissionDTOs.setPage((int) permissions.getNumber() + 1);
        return permissionDTOs;
    }

    @Override
    public PermissionDTO handleGetPermissionById(UUID id) {
        return this.modelMapper.map(
                this.permissionRepository.findById(id)
                        .orElseThrow(() -> new CustomRuntimeException("Not found permission by id " + id)),
                PermissionDTO.class);
    }

    @Override
    public PermissionDTO handleCreatePermission(PermissionDTO permissionDTO) {
        PermissionEntity permission = this.modelMapper.map(permissionDTO, PermissionEntity.class);
        permission = this.permissionRepository.save(permission);
        return this.modelMapper.map(permission, PermissionDTO.class);
    }

    @Override
    public PermissionDTO handleUpdatePermission(PermissionDTO permissionDTO) {
        PermissionEntity beModifiedPermission = this.permissionRepository.findById(permissionDTO.getId())
                .orElseThrow(() -> new CustomRuntimeException("Not found permission by id " + permissionDTO.getId()));
        PermissionEntity permission = this.modelMapper.map(permissionDTO, PermissionEntity.class);
        MergeUtils.mergeObject(permission, beModifiedPermission);
        beModifiedPermission = this.permissionRepository.save(beModifiedPermission);
        return this.modelMapper.map(beModifiedPermission, PermissionDTO.class);
    }

    @Override
    public void handleDeletePermission(List<UUID> ids) {
        this.permissionRepository.deleteByIdIn(ids);
    }
}