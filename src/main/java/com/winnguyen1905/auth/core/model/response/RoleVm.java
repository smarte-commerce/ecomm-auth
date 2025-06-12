package com.winnguyen1905.auth.core.model.response;

import java.util.UUID;

import com.winnguyen1905.auth.core.model.request.AbstractModel;

import lombok.Builder;

@Builder
public record RoleVm(UUID id, String name, String code) implements AbstractModel {
    @Builder
    public RoleVm(UUID id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
}
