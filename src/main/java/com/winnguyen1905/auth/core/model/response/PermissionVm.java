package com.winnguyen1905.auth.core.model.response;

import java.util.UUID;

import lombok.Builder;

@Builder
public record PermissionVm(UUID id, String name, String code, String apiPath, String method, String module) {}
