package com.winnguyen1905.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winnguyen1905.auth.common.annotation.ResponseMessage;
import com.winnguyen1905.auth.core.model.response.RestResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ModifyResponseContentFilter implements WebFilter {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    return chain.filter(exchange)
        .then(Mono.defer(() -> handleResponse(exchange)));
  }

  private Mono<Void> handleResponse(ServerWebExchange exchange) {
    ServerHttpResponse response = exchange.getResponse();

    if (response.isCommitted()) {
      return Mono.empty();
    }

    HttpStatusCode statusCode = getHttpStatus(response);

    return response.writeWith(Mono.fromCallable(() -> {
      Object content = statusCode.isError()
          ? "An error occurred."
          : buildRestResponse(exchange, statusCode);
      return createDataBuffer(response, content);
    }));
  }

  private HttpStatusCode getHttpStatus(ServerHttpResponse response) {
    return response.getStatusCode() != null ? response.getStatusCode() : HttpStatus.INTERNAL_SERVER_ERROR;
  }

  private RestResponse<Object> buildRestResponse(ServerWebExchange exchange, HttpStatusCode statusCode) {
    Object body = exchange.getAttribute("responseBody");
    MethodParameter returnType = (MethodParameter) exchange.getAttribute("returnType");

    String message = null;
    if (returnType != null) {
      ResponseMessage metaMessage = returnType.getMethodAnnotation(ResponseMessage.class);
      if (metaMessage != null) {
        message = metaMessage.message();
      }
    }

    return RestResponse.builder()
        .data(body)
        .message(message)
        .statusCode(statusCode.value())
        .build();
  }

  private DataBuffer createDataBuffer(ServerHttpResponse response, Object content) throws JsonProcessingException {
    String jsonResponse = objectMapper.writeValueAsString(content);
    response.getHeaders().add("Content-Type", "application/json");
    return response.bufferFactory().wrap(jsonResponse.getBytes());
  }
}
