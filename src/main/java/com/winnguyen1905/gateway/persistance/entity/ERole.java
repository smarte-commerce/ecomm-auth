package com.winnguyen1905.gateway.persistance.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class ERole extends EBaseAudit {
  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "code", unique = true, nullable = false)
  private String code;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  @JoinTable(name = "role_details", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
  private Set<EPermission> permissions = new HashSet<>();

  @OneToMany(mappedBy = "role")
  private Set<EUserCredentials> users = new HashSet<>();
}
