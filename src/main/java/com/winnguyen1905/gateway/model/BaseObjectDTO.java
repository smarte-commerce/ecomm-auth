package com.winnguyen1905.gateway.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat; 

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseObjectDTO<T> extends AbstractModel {
    
    private UUID id;

    private String createdBy;

    private String updatedBy;

    private Boolean isDeleted;

    @JsonFormat(pattern = "HH-mm-ss a dd-MM-yyyy", timezone = "GMT+7")
    private String createdDate;

    @JsonFormat(pattern = "HH-mm-ss a dd-MM-yyyy", timezone = "GMT+7")
    private String updatedDate;

}