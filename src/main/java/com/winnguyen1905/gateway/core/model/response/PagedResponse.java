package com.winnguyen1905.gateway.core.model.response;

import java.util.List;

import com.winnguyen1905.gateway.core.model.AbstractModel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PagedResponse<T> extends AbstractModel {
    private int maxPageItems;

    private int page;

    private int size;

    private List<T> results;

    private int totalElements;

    private int totalPages;
}
