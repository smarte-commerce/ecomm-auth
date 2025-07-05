package com.winnguyen1905.auth.core.model.response;

import java.util.UUID;

public record CustomerVm(
    UUID id,
    UUID accountId,
    String customerName,
    String customerAddress,
    String customerPhone,
    String customerEmail,
    String customerLogo,
    Boolean customerStatus
) {} 
