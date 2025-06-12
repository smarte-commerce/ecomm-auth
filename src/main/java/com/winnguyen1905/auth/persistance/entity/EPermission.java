package com.winnguyen1905.auth.persistance.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EPermission {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  protected UUID id;

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

  @Column(nullable = true)
  private Integer left;

  @Column(nullable = true)
  private Integer right;

  @ManyToMany(mappedBy = "permissions")
  private Set<ERole> roles = new HashSet<>();
}
