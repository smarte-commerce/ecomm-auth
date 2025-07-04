package com.winnguyen1905.auth.core.controller;

import org.springframework.web.bind.annotation.RestController;

import com.winnguyen1905.auth.common.annotation.ResponseMessage;
import com.winnguyen1905.auth.core.model.response.AccountVm;
import com.winnguyen1905.auth.core.model.response.PagedResponse;
import com.winnguyen1905.auth.core.service.AccountServiceInterface;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import reactor.core.publisher.Mono;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
  
  private final AccountServiceInterface accountService;

  @GetMapping("/{username}")
  @ResponseMessage(message = "Get user by username success")
  public Mono<ResponseEntity<AccountVm>> getUserByUsername(@PathVariable String username) {
    return this.accountService.getUserByUsername(username)
        .map(user -> ResponseEntity.ok(user));
  }

  @GetMapping("/id/{id}")
  @ResponseMessage(message = "Get user by id success")
  public Mono<ResponseEntity<AccountVm>> getUserById(@PathVariable UUID id) {
    return this.accountService.getUserById(id)
        .map(user -> ResponseEntity.ok(user));
  }
  
  @GetMapping
  @ResponseMessage(message = "Get all users success")
  public Mono<ResponseEntity<PagedResponse<AccountVm>>> getAllUsers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "username") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDir) {
    
    return this.accountService.getAllUsers(page, size, sortBy, sortDir)
        .map(response -> ResponseEntity.ok(response));
  }
  
  @GetMapping("/search")
  @ResponseMessage(message = "Search users success")
  public Mono<ResponseEntity<PagedResponse<AccountVm>>> searchUsers(
      @RequestParam String keyword,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    
    return this.accountService.searchUsers(keyword, page, size)
        .map(response -> ResponseEntity.ok(response));
  }
  
  @PostMapping
  @ResponseMessage(message = "Create user success")
  public Mono<ResponseEntity<AccountVm>> createUser(@RequestBody AccountVm accountVm) {
    return this.accountService.createUser(accountVm)
        .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user));
  }
  
  @PutMapping("/{id}")
  @ResponseMessage(message = "Update user success")
  public Mono<ResponseEntity<AccountVm>> updateUser(
      @PathVariable UUID id,
      @RequestBody AccountVm accountVm) {
    
    return this.accountService.updateUser(id, accountVm)
        .map(user -> ResponseEntity.ok(user));
  }
  
  @DeleteMapping("/{id}")
  @ResponseMessage(message = "Delete user success")
  public Mono<ResponseEntity<Void>> deleteUser(@PathVariable UUID id) {
    return this.accountService.deleteUser(id)
        .thenReturn(ResponseEntity.noContent().build());
  }
  
  @DeleteMapping
  @ResponseMessage(message = "Delete multiple users success")
  public Mono<ResponseEntity<Void>> deleteUsers(@RequestBody List<UUID> ids) {
    return this.accountService.deleteUsers(ids)
        .thenReturn(ResponseEntity.noContent().build());
  }
  
  @PatchMapping("/{id}/activate")
  @ResponseMessage(message = "Activate user success")
  public Mono<ResponseEntity<AccountVm>> activateUser(@PathVariable UUID id) {
    return this.accountService.activateUser(id)
        .map(user -> ResponseEntity.ok(user));
  }
  
  @PatchMapping("/{id}/deactivate")
  @ResponseMessage(message = "Deactivate user success")
  public Mono<ResponseEntity<AccountVm>> deactivateUser(@PathVariable UUID id) {
    return this.accountService.deactivateUser(id)
        .map(user -> ResponseEntity.ok(user));
  }
  
  @PatchMapping("/{accountId}/customer/{customerId}")
  @ResponseMessage(message = "Assign customer to account success")
  public Mono<ResponseEntity<AccountVm>> assignCustomerToAccount(
      @PathVariable UUID accountId,
      @PathVariable UUID customerId) {
    
    return this.accountService.assignCustomerToAccount(accountId, customerId)
        .map(account -> ResponseEntity.ok(account));
  }
  
  @PatchMapping("/{accountId}/vendor/{vendorId}")
  @ResponseMessage(message = "Assign vendor to account success")
  public Mono<ResponseEntity<AccountVm>> assignVendorToAccount(
      @PathVariable UUID accountId,
      @PathVariable UUID vendorId) {
    
    return this.accountService.assignVendorToAccount(accountId, vendorId)
        .map(account -> ResponseEntity.ok(account));
  }
}
