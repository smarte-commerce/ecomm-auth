package com.winnguyen1905.auth.core.model.response;

import java.util.UUID;

public record ShopVm(
    UUID id,
    String name,
    String description,
    String address,
    String phone,
    String email,
    String logo,
    Boolean status,
    UUID vendorId
) {} 
