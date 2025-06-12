package com.winnguyen1905.auth.core.model.response;

import java.util.UUID;

import com.winnguyen1905.auth.core.model.request.AbstractModel;

import lombok.Builder;

@Builder
public record PermissionVm(UUID id, String name, String code, String apiPath, String method, String module) implements AbstractModel {
    @Builder
    public PermissionVm(UUID id, String name, String code, String apiPath, String method, String module) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.apiPath = apiPath;
        this.method = method;
        this.module = module;
    }
}
