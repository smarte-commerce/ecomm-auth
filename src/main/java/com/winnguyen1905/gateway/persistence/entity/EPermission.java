package com.winnguyen1905.gateway.persistence.entity;

import java.util.HashSet;
import java.util.Set; 

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "permissions")
public class EPermission extends EBaseAudit {

    @Column(nullable = true, name = "name")
    private String name;

    @Column(nullable = true, name = "code")
    private String code;

    @Column(nullable = true, name = "api_path")
    private String apiPath;

    @Column(nullable = true, name = "method")
    private String method;

    @Column(nullable = true, name = "module")
    private String module;

    private Integer left;

    private Integer right;

    // @ManyToMany(mappedBy = "permissions", cascade = CascadeType.PERSIST)
    // private Set<RoleEntity> roles = new HashSet<>();

    // @PrePersist
    // protected void prePersist() {
    //     String create = this.apiPath + " " + this.method + " " + this.module;
    //     this.code = StringUtils.convertCamelToSnake(create);
    // }

}