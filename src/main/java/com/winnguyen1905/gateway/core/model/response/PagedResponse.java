package com.winnguyen1905.gateway.core.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winnguyen1905.gateway.core.model.AbstractModel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PagedResponse<T> extends AbstractModel {
    private Integer maxPageItems;

    private Integer page;

    private Integer size;

    @JsonProperty("results")
    private List<T> results;

    private Integer totalElements;

    private Integer totalPages;
}