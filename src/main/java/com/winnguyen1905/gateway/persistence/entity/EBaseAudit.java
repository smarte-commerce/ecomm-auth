package com.winnguyen1905.gateway.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class EBaseAudit extends EBase {
    @JsonIgnore
    @Column(name = "created_by", nullable = true)
    private String createdBy;

    @JsonIgnore
    @Column(name = "updated_by", nullable = true)
    private String updatedBy;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private Instant createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date", updatable = true)
    private Instant updatedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof EBaseAudit))
            return false;
        if (!super.equals(o))
            return false;
        EBaseAudit that = (EBaseAudit) o;
        return createdBy.equals(that.createdBy) && updatedBy.equals(that.updatedBy) &&
                createdDate.equals(that.createdDate) && updatedDate.equals(that.updatedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), createdBy, updatedBy, createdDate, updatedDate);
    }

    // public String findCurrentUser() {
    //     return SecurityUtils.getCurrentUserLogin().orElse("Unknown");
    // }

    // @PrePersist
    // protected void prePersist() {
    //     this.setIsDeleted(false);
    //     this.setCreatedBy(findCurrentUser());
    // }

    // @PreUpdate
    // protected void preUpdate() {
    //     this.setUpdatedBy(findCurrentUser());
    // }

    // @PreRemove
    // protected void preRemove() {
        // this.setUpdatedBy(findSystemUser());
    // }
}