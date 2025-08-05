package com.winnguyen1905.auth.core.controller;

import java.util.List;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RestController;

import com.winnguyen1905.auth.common.annotation.ResponseMessage;
import com.winnguyen1905.auth.core.model.response.CustomerVm;
import com.winnguyen1905.auth.core.model.response.PagedResponse;
import com.winnguyen1905.auth.core.service.CustomerServiceInterface;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("customers")
public class CustomerController {

  // private final CustomerServiceInterface customerService;

  // @GetMapping("/{id}")
  // @ResponseMessage(message = "Get customer by id success")
  // public Mono<ResponseEntity<CustomerVm>> getCustomerById(@PathVariable UUID id) {
  //   return this.customerService.getCustomerById(id)
  //       .map(customer -> ResponseEntity.ok(customer));
  // }

  // @GetMapping
  // @ResponseMessage(message = "Get all customers success")
  // public Mono<ResponseEntity<PagedResponse<CustomerVm>>> getAllCustomers(
  //     @RequestParam(defaultValue = "0") int page,
  //     @RequestParam(defaultValue = "10") int size,
  //     @RequestParam(defaultValue = "customerName") String sortBy,
  //     @RequestParam(defaultValue = "asc") String sortDir) {

  //   return this.customerService.getAllCustomers(page, size, sortBy, sortDir)
  //       .map(response -> ResponseEntity.ok(response));
  // }

  // @GetMapping("/search")
  // @ResponseMessage(message = "Search customers success")
  // public Mono<ResponseEntity<PagedResponse<CustomerVm>>> searchCustomers(
  //     @RequestParam String keyword,
  //     @RequestParam(defaultValue = "0") int page,
  //     @RequestParam(defaultValue = "10") int size) {

  //   return this.customerService.searchCustomers(keyword, page, size)
  //       .map(response -> ResponseEntity.ok(response));
  // }

  // @GetMapping("/account/{accountId}")
  // @ResponseMessage(message = "Get customer by account id success")
  // public Mono<ResponseEntity<CustomerVm>> getCustomerByAccountId(@PathVariable UUID accountId) {
  //   return this.customerService.getCustomerByAccountId(accountId)
  //       .map(customer -> ResponseEntity.ok(customer));
  // }

  // @PostMapping
  // @ResponseMessage(message = "Create customer success")
  // public Mono<ResponseEntity<CustomerVm>> createCustomer(@RequestBody CustomerVm customerVm) {
  //   return this.customerService.createCustomer(customerVm)
  //       .map(customer -> ResponseEntity.status(HttpStatus.CREATED).body(customer));
  // }

  // @PutMapping("/{id}")
  // @ResponseMessage(message = "Update customer success")
  // public Mono<ResponseEntity<CustomerVm>> updateCustomer(
  //     @PathVariable UUID id,
  //     @RequestBody CustomerVm customerVm) {

  //   return this.customerService.updateCustomer(id, customerVm)
  //       .map(customer -> ResponseEntity.ok(customer));
  // }

  // @DeleteMapping("/{id}")
  // @ResponseMessage(message = "Delete customer success")
  // public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable UUID id) {
  //   return this.customerService.deleteCustomer(id)
  //       .thenReturn(ResponseEntity.noContent().build());
  // }

  // @DeleteMapping
  // @ResponseMessage(message = "Delete multiple customers success")
  // public Mono<ResponseEntity<Void>> deleteCustomers(@RequestBody List<UUID> ids) {
  //   return this.customerService.deleteCustomers(ids)
  //       .thenReturn(ResponseEntity.noContent().build());
  // }

  // @PatchMapping("/{id}/activate")
  // @ResponseMessage(message = "Activate customer success")
  // public Mono<ResponseEntity<CustomerVm>> activateCustomer(@PathVariable UUID id) {
  //   return this.customerService.activateCustomer(id)
  //       .map(customer -> ResponseEntity.ok(customer));
  // }

  // @PatchMapping("/{id}/deactivate")
  // @ResponseMessage(message = "Deactivate customer success")
  // public Mono<ResponseEntity<CustomerVm>> deactivateCustomer(@PathVariable UUID id) {
  //   return this.customerService.deactivateCustomer(id)
  //       .map(customer -> ResponseEntity.ok(customer));
  // }
}
