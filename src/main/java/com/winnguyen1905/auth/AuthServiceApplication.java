package com.winnguyen1905.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServiceApplication {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(AuthServiceApplication.class);
    app.setWebApplicationType(WebApplicationType.REACTIVE);
    app.run(args);
  }

}
