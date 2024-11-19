package com.winnguyen1905.gateway.core.model;

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
public class WebsocketEvent extends AbstractModel {
  String message;
  String eventType;
  String eventSubType;
}
