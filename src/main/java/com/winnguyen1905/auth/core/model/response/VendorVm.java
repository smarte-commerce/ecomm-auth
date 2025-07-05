package com.winnguyen1905.auth.core.model.response;

import java.util.UUID;

public record VendorVm(
    UUID id,
    UUID accountId,
    String vendorName,
    String vendorDescription,
    String vendorAddress,
    String vendorPhone,
    String vendorEmail,
    String vendorLogo,
    Boolean vendorStatus
) {} 
