package com.winnguyen1905.gateway.persistance.entity;

import java.util.HashSet;
import java.util.Set;

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

  // @Column(nullable = true)
  // private Integer left;

  // @Column(nullable = true)
  // private Integer right;

  @ManyToMany(mappedBy = "permissions")
  private Set<ERole> roles = new HashSet<>();

  @Override
  @PrePersist
  protected void prePersist() {
    String create = this.apiPath + " " + this.method + " " + this.module;
  // this.code = StringUtils.convertCamelToSnake(create);
  }

}
