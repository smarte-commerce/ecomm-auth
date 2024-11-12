package com.winnguyen1905.gateway.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("shops")
public class ShopController {
  @GetMapping("/")
  public String getMethodName(@RequestParam String param) {
    return new String();
  }
}
