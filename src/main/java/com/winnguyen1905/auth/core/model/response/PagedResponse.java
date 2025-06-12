package com.winnguyen1905.auth.core.model.response;

import java.util.List;

import com.winnguyen1905.auth.core.model.request.AbstractModel;

import lombok.Builder;

@Builder
public record PagedResponse<T>(
    int maxPageItems,
    int page,
    int size,
    List<T> results,
    int totalElements,
    int totalPages) implements AbstractModel {

    @Builder
    public PagedResponse(
        int maxPageItems,
        int page,
        int size,
        List<T> results,
        int totalElements,
        int totalPages) {
      this.maxPageItems = maxPageItems;
      this.page = page;
      this.size = size;
      this.results = results;
      this.totalElements = totalElements;
      this.totalPages = totalPages;
    }
}
