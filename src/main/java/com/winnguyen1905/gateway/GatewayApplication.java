package com.winnguyen1905.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

@Configuration
@EnableWebFlux
@SpringBootApplication
@EnableAspectJAutoProxy
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	WebSocketHandlerAdapter webSocketHandlerAdapter(){
		return new WebSocketHandlerAdapter();
	}
	@Bean
	public WebProperties.Resources resources() {
		return new WebProperties.Resources();
	}

}
