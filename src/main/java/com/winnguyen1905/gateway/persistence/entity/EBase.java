package com.winnguyen1905.gateway.persistence.entity;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@MappedSuperclass
public abstract class EBase implements Serializable {
    private static final long serialVersionUID = -863164858986274318L;

    @Id
    @Column(columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "custom-uuid")
    private UUID id;

    @Column(name = "is_deleted", updatable = true)
    private Boolean isDeleted;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EBase)) return false;
        EBase that = (EBase) o;
        return id.equals(that.id);
    }
}