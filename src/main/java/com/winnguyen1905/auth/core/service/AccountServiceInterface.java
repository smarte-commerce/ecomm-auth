package com.winnguyen1905.auth.core.service;

import java.util.List;
import java.util.UUID;

import com.winnguyen1905.auth.core.model.response.AccountVm;
import com.winnguyen1905.auth.core.model.response.PagedResponse;

import reactor.core.publisher.Mono;

public interface AccountServiceInterface {
    // Basic CRUD operations
    Mono<AccountVm> getUserByUsername(String username);
    Mono<AccountVm> getUserById(UUID id);
    Mono<AccountVm> createUser(AccountVm accountVm);
    Mono<AccountVm> updateUser(UUID id, AccountVm accountVm);
    Mono<Void> deleteUser(UUID id);
    Mono<Void> deleteUsers(List<UUID> ids);
    
    // Advanced operations
    Mono<PagedResponse<AccountVm>> getAllUsers(int page, int size, String sortBy, String sortDir);
    Mono<PagedResponse<AccountVm>> searchUsers(String keyword, int page, int size);
    
    // Status management
    Mono<AccountVm> activateUser(UUID id);
    Mono<AccountVm> deactivateUser(UUID id);
    
    // Customer and Vendor related operations
    Mono<AccountVm> assignCustomerToAccount(UUID accountId, UUID customerId);
    Mono<AccountVm> assignVendorToAccount(UUID accountId, UUID vendorId);
} 
