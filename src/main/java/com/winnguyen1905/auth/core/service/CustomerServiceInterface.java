package com.winnguyen1905.auth.core.service;

import java.util.List;
import java.util.UUID;

import com.winnguyen1905.auth.core.model.response.CustomerVm;
import com.winnguyen1905.auth.core.model.response.PagedResponse;

import reactor.core.publisher.Mono;

public interface CustomerServiceInterface {
    // Basic CRUD operations
    Mono<CustomerVm> getCustomerById(UUID id);
    Mono<CustomerVm> createCustomer(CustomerVm customerVm);
    Mono<CustomerVm> updateCustomer(UUID id, CustomerVm customerVm);
    Mono<Void> deleteCustomer(UUID id);
    Mono<Void> deleteCustomers(List<UUID> ids);
    
    // Advanced operations
    Mono<PagedResponse<CustomerVm>> getAllCustomers(int page, int size, String sortBy, String sortDir);
    Mono<PagedResponse<CustomerVm>> searchCustomers(String keyword, int page, int size);
    
    // Status management
    Mono<CustomerVm> activateCustomer(UUID id);
    Mono<CustomerVm> deactivateCustomer(UUID id);
    
    // Account related operations
    Mono<CustomerVm> getCustomerByAccountId(UUID accountId);
} 
