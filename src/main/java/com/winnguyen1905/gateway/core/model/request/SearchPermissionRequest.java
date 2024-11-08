package com.winnguyen1905.gateway.core.model.request;

import com.winnguyen1905.gateway.core.model.BaseObject;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SearchPermissionRequest extends BaseObject<SearchPermissionRequest> {
  private String name;
  private String code;
  private String apiPath;
  private String method;
  private String module;
}
