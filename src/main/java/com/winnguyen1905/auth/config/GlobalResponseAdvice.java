package com.winnguyen1905.auth.config;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.winnguyen1905.auth.common.annotation.ResponseMessage;
import com.winnguyen1905.auth.core.model.response.RestResponse;

import reactor.core.publisher.Mono;

/**
 * Utility class for wrapping responses in RestResponse format
 * Since automatic wrapping with WebFlux is complex, we provide utility methods
 * for controllers to use when they want to return wrapped responses
 */
@RestControllerAdvice
public class GlobalResponseAdvice {

  /**
   * Wrap a successful response with RestResponse format
   */
  public static <T> Mono<ResponseEntity<RestResponse<T>>> wrapSuccess(Mono<T> data, String message) {
    return data.map(body -> {
      RestResponse<T> wrappedResponse = RestResponse.<T>builder()
          .data(body)
          .message(message)
          .statusCode(HttpStatus.OK.value())
          .build();

      return ResponseEntity.ok(wrappedResponse);
    }).onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(RestResponse.<T>builder()
            .error("Internal server error")
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .build()));
  }

  /**
   * Wrap a successful response with RestResponse format and custom status
   */
  public static <T> Mono<ResponseEntity<RestResponse<T>>> wrapSuccess(Mono<T> data, String message, HttpStatus status) {
    return data.map(body -> {
      RestResponse<T> wrappedResponse = RestResponse.<T>builder()
          .data(body)
          .message(message)
          .statusCode(status.value())
          .build();

      return ResponseEntity.status(status).body(wrappedResponse);
    }).onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(RestResponse.<T>builder()
            .error("Internal server error")
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .build()));
  }

  /**
   * Wrap an error response
   */
  public static <T> ResponseEntity<RestResponse<T>> wrapError(String error, HttpStatus status) {
    RestResponse<T> errorResponse = RestResponse.<T>builder()
        .error(error)
        .statusCode(status.value())
        .build();

    return ResponseEntity.status(status).body(errorResponse);
  }

  /**
   * Extract message from @ResponseMessage annotation
   */
  public static String extractMessage(MethodParameter methodParameter) {
    if (methodParameter != null) {
      ResponseMessage metaMessage = methodParameter.getMethodAnnotation(ResponseMessage.class);
      if (metaMessage != null) {
        return metaMessage.message();
      }
    }
    return null;
  }
}
