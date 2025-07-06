package com.winnguyen1905.auth.core.service;

import java.util.List;
import java.util.UUID;

import com.winnguyen1905.auth.core.model.response.PagedResponse;
import com.winnguyen1905.auth.core.model.response.ShopVm;

import reactor.core.publisher.Mono;

public interface ShopServiceInterface {
    // Basic CRUD operations
    Mono<ShopVm> getShopById(UUID id);
    Mono<ShopVm> createShop(ShopVm shopVm);
    Mono<ShopVm> updateShop(UUID id, ShopVm shopVm);
    Mono<Void> deleteShop(UUID id);
    Mono<Void> deleteShops(List<UUID> ids);
    
    // Advanced operations
    Mono<PagedResponse<ShopVm>> getAllShops(int page, int size, String sortBy, String sortDir);
    Mono<PagedResponse<ShopVm>> searchShops(String keyword, int page, int size);
    
    // Vendor related operations
    Mono<List<ShopVm>> getShopsByVendorId(UUID vendorId);
} 
