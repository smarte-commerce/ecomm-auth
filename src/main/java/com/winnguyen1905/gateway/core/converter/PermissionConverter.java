package com.winnguyen1905.gateway.core.converter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.winnguyen1905.gateway.core.model.request.SearchPermissionRequest;
import com.winnguyen1905.gateway.persistence.entity.EPermission; 
import com.winnguyen1905.gateway.persistence.repository.specification.QuerySpecification;


@Component
public class PermissionConverter {
    public Specification<EPermission> toPermissionSpec(SearchPermissionRequest permissionSearchRequest) {
        Field[] fields = permissionSearchRequest.getClass().getDeclaredFields();
        List<Specification<EPermission>> specList = new ArrayList<>();
        java.util.Arrays.asList(fields).forEach(field -> {
            try {
                String fieldName = field.getName();
                field.setAccessible(true);
                Object value = field.get(permissionSearchRequest);
                if(value.getClass().getName().equals("java.lang.String"))
                specList.add(QuerySpecification.isValueLike((String) value, fieldName, null));
            } catch (IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        });
        return Specification.allOf(specList);
    }
}