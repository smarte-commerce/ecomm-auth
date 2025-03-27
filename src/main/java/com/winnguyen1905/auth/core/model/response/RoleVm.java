package com.winnguyen1905.auth.core.model.response;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public record RoleVm(UUID id, String name, String code) {}
