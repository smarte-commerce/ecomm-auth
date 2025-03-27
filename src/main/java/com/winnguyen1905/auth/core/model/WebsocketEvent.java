package com.winnguyen1905.auth.core.model;

import com.winnguyen1905.auth.core.model.request.AbstractModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebsocketEvent implements AbstractModel {
  String message;
  String eventType;
  String eventSubType;
}
