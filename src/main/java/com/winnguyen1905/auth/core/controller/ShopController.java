package com.winnguyen1905.auth.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.winnguyen1905.auth.common.annotation.ResponseMessage;
import com.winnguyen1905.auth.core.model.response.ShopVm;
import com.winnguyen1905.auth.core.model.response.PagedResponse;
import com.winnguyen1905.auth.core.service.ShopServiceInterface;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("shops")
public class ShopController {
  
  // private final ShopServiceInterface shopService;

  // @GetMapping("/health")
  // @ResponseMessage(message = "Shop service health check")
  // public Mono<ResponseEntity<String>> healthCheck() {
  //   return Mono.just(ResponseEntity.ok("Shop service is healthy"));
  // }
  
  // @GetMapping("/{id}")
  // @ResponseMessage(message = "Get shop by id success")
  // public Mono<ResponseEntity<ShopVm>> getShopById(@PathVariable UUID id) {
  //   return this.shopService.getShopById(id)
  //       .map(shop -> ResponseEntity.ok(shop));
  // }
  
  // @GetMapping
  // @ResponseMessage(message = "Get all shops success")
  // public Mono<ResponseEntity<PagedResponse<ShopVm>>> getAllShops(
  //     @RequestParam(defaultValue = "0") int page,
  //     @RequestParam(defaultValue = "10") int size,
  //     @RequestParam(defaultValue = "name") String sortBy,
  //     @RequestParam(defaultValue = "asc") String sortDir) {
    
  //   return this.shopService.getAllShops(page, size, sortBy, sortDir)
  //       .map(response -> ResponseEntity.ok(response));
  // }
  
  // @GetMapping("/search")
  // @ResponseMessage(message = "Search shops success")
  // public Mono<ResponseEntity<PagedResponse<ShopVm>>> searchShops(
  //     @RequestParam String keyword,
  //     @RequestParam(defaultValue = "0") int page,
  //     @RequestParam(defaultValue = "10") int size) {
    
  //   return this.shopService.searchShops(keyword, page, size)
  //       .map(response -> ResponseEntity.ok(response));
  // }
  
  // @GetMapping("/vendor/{vendorId}")
  // @ResponseMessage(message = "Get shops by vendor id success")
  // public Mono<ResponseEntity<List<ShopVm>>> getShopsByVendorId(@PathVariable UUID vendorId) {
  //   return this.shopService.getShopsByVendorId(vendorId)
  //       .map(shops -> ResponseEntity.ok(shops));
  // }
  
  // @PostMapping
  // @ResponseMessage(message = "Create shop success")
  // public Mono<ResponseEntity<ShopVm>> createShop(@RequestBody ShopVm shopVm) {
  //   return this.shopService.createShop(shopVm)
  //       .map(shop -> ResponseEntity.status(HttpStatus.CREATED).body(shop));
  // }
  
  // @PutMapping("/{id}")
  // @ResponseMessage(message = "Update shop success")
  // public Mono<ResponseEntity<ShopVm>> updateShop(
  //     @PathVariable UUID id,
  //     @RequestBody ShopVm shopVm) {
    
  //   return this.shopService.updateShop(id, shopVm)
  //       .map(shop -> ResponseEntity.ok(shop));
  // }
  
  // @DeleteMapping("/{id}")
  // @ResponseMessage(message = "Delete shop success")
  // public Mono<ResponseEntity<Void>> deleteShop(@PathVariable UUID id) {
  //   return this.shopService.deleteShop(id)
  //       .thenReturn(ResponseEntity.noContent().build());
  // }
  
  // @DeleteMapping
  // @ResponseMessage(message = "Delete multiple shops success")
  // public Mono<ResponseEntity<Void>> deleteShops(@RequestBody List<UUID> ids) {
  //   return this.shopService.deleteShops(ids)
  //       .thenReturn(ResponseEntity.noContent().build());
  // }
}
