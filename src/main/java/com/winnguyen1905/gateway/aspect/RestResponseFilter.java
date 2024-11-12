package com.winnguyen1905.gateway.aspect;

import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.winnguyen1905.gateway.core.model.response.RestResponse;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
@SuppressWarnings({ "null", "rawtypes" })
public class RestResponseFilter implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(MethodParameter returnType, Class converterType) {
    return true;
  }

  @Override
  @Nullable
  public Object beforeBodyWrite(
      Object body, MethodParameter returnType, MediaType selectedContentType,
      Class selectedConverterType, ServerHttpRequest request,
      ServerHttpResponse response) {

    if (body == null || body instanceof Resource)
      return body;

    HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
    int statusCode = servletResponse.getStatus();
    RestResponse<Object> restResponse = RestResponse.builder().statusCode(statusCode).build();
    if (statusCode > 399)
      return body;
    else {
      // restResponse.setData((Object) body);
      // if (returnType.getMethodAnnotation(MetaMessage.class) instanceof MetaMessage message)
      //   restResponse.setMessage(message.message());
    }

    return restResponse;
  }

}
