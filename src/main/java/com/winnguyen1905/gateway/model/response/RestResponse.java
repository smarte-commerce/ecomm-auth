package com.winnguyen1905.gateway.model.response;

import com.winnguyen1905.gateway.model.AbstractModel;

import lombok.*;

@Getter
@Setter
@Builder
public class RestResponse<T> extends AbstractModel {
    private Integer statusCode;
    private String error;
    private Object message;
    private T data;
}