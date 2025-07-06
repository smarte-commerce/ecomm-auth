package com.winnguyen1905.auth.core.service;

import java.util.List;
import java.util.UUID;

import com.winnguyen1905.auth.core.model.response.PagedResponse;
import com.winnguyen1905.auth.core.model.response.VendorVm;

import reactor.core.publisher.Mono;

public interface VendorServiceInterface {
    // Basic CRUD operations
    Mono<VendorVm> getVendorById(UUID id);
    Mono<VendorVm> createVendor(VendorVm vendorVm);
    Mono<VendorVm> updateVendor(UUID id, VendorVm vendorVm);
    Mono<Void> deleteVendor(UUID id);
    Mono<Void> deleteVendors(List<UUID> ids);
    
    // Advanced operations
    Mono<PagedResponse<VendorVm>> getAllVendors(int page, int size, String sortBy, String sortDir);
    Mono<PagedResponse<VendorVm>> searchVendors(String keyword, int page, int size);
    
    // Status management
    Mono<VendorVm> activateVendor(UUID id);
    Mono<VendorVm> deactivateVendor(UUID id);
    
    // Account related operations
    Mono<VendorVm> getVendorByAccountId(UUID accountId);
} 
