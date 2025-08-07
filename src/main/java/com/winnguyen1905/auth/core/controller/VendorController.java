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
import com.winnguyen1905.auth.core.model.response.PagedResponse;
import com.winnguyen1905.auth.core.model.response.VendorVm;
import com.winnguyen1905.auth.core.service.VendorServiceInterface;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("vendors")
public class VendorController {

  // private final VendorServiceInterface vendorService;

  // @GetMapping("/{id}")
  // @ResponseMessage(message = "Get vendor by id success")
  // public Mono<ResponseEntity<VendorVm>> getVendorById(@PathVariable UUID id) {
  // return this.vendorService.getVendorById(id)
  // .map(vendor -> ResponseEntity.ok(vendor));
  // }

  // @GetMapping
  // @ResponseMessage(message = "Get all vendors success")
  // public Mono<ResponseEntity<PagedResponse<VendorVm>>> getAllVendors(
  // @RequestParam(defaultValue = "0") int page,
  // @RequestParam(defaultValue = "10") int size,
  // @RequestParam(defaultValue = "vendorName") String sortBy,
  // @RequestParam(defaultValue = "asc") String sortDir) {

  // return this.vendorService.getAllVendors(page, size, sortBy, sortDir)
  // .map(response -> ResponseEntity.ok(response));
  // }

  // @GetMapping("/search")
  // @ResponseMessage(message = "Search vendors success")
  // public Mono<ResponseEntity<PagedResponse<VendorVm>>> searchVendors(
  // @RequestParam String keyword,
  // @RequestParam(defaultValue = "0") int page,
  // @RequestParam(defaultValue = "10") int size) {

  // return this.vendorService.searchVendors(keyword, page, size)
  // .map(response -> ResponseEntity.ok(response));
  // }

  // @GetMapping("/account/{accountId}")
  // @ResponseMessage(message = "Get vendor by account id success")
  // public Mono<ResponseEntity<VendorVm>> getVendorByAccountId(@PathVariable UUID
  // accountId) {
  // return this.vendorService.getVendorByAccountId(accountId)
  // .map(vendor -> ResponseEntity.ok(vendor));
  // }

  // @PostMapping
  // @ResponseMessage(message = "Create vendor success")
  // public Mono<ResponseEntity<VendorVm>> createVendor(@RequestBody VendorVm
  // vendorVm) {
  // return this.vendorService.createVendor(vendorVm)
  // .map(vendor -> ResponseEntity.status(HttpStatus.CREATED).body(vendor));
  // }

  // @PutMapping("/{id}")
  // @ResponseMessage(message = "Update vendor success")
  // public Mono<ResponseEntity<VendorVm>> updateVendor(
  // @PathVariable UUID id,
  // @RequestBody VendorVm vendorVm) {

  // return this.vendorService.updateVendor(id, vendorVm)
  // .map(vendor -> ResponseEntity.ok(vendor));
  // }

  // @DeleteMapping("/{id}")
  // @ResponseMessage(message = "Delete vendor success")
  // public Mono<ResponseEntity<Void>> deleteVendor(@PathVariable UUID id) {
  // return this.vendorService.deleteVendor(id)
  // .thenReturn(ResponseEntity.noContent().build());
  // }

  // @DeleteMapping
  // @ResponseMessage(message = "Delete multiple vendors success")
  // public Mono<ResponseEntity<Void>> deleteVendors(@RequestBody List<UUID> ids)
  // {
  // return this.vendorService.deleteVendors(ids)
  // .thenReturn(ResponseEntity.noContent().build());
  // }

  // @PatchMapping("/{id}/activate")
  // @ResponseMessage(message = "Activate vendor success")
  // public Mono<ResponseEntity<VendorVm>> activateVendor(@PathVariable UUID id) {
  // return this.vendorService.activateVendor(id)
  // .map(vendor -> ResponseEntity.ok(vendor));
  // }

  // @PatchMapping("/{id}/deactivate")
  // @ResponseMessage(message = "Deactivate vendor success")
  // public Mono<ResponseEntity<VendorVm>> deactivateVendor(@PathVariable UUID id)
  // {
  // return this.vendorService.deactivateVendor(id)
  // .map(vendor -> ResponseEntity.ok(vendor));
  // }
}
