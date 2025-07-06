package com.winnguyen1905.auth.persistance.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permissions", schema = "public")
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
