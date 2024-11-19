package com.winnguyen1905.gateway.core.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseObject<T> extends AbstractModel {
  protected UUID id;

  protected String createdBy;

  protected String updatedBy;

  protected Boolean isDeleted;

  @JsonFormat(pattern = "HH-mm-ss a dd-MM-yyyy", timezone = "GMT+7")
  protected String createdDate;

  @JsonFormat(pattern = "HH-mm-ss a dd-MM-yyyy", timezone = "GMT+7")
  protected String updatedDate;
}
