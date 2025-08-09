package com.winnguyen1905.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {
  // The GlobalResponseAdvice will be auto-discovered through
  // @RestControllerAdvice
}
