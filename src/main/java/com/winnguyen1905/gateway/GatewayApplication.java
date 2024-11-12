package com.winnguyen1905.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class GatewayApplication {
  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
  }

  // @Bean
  // WebSocketHandlerAdapter webSocketHandlerAdapter() {
  //   return new WebSocketHandlerAdapter();
  // }

  // @Bean
  // public WebProperties.Resources resources() {
  //   return new WebProperties.Resources();
  // }

}
