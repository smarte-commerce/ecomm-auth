package com.winnguyen1905.auth.persistance.entity;

import java.util.UUID;

import com.winnguyen1905.auth.common.constant.AccountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "roles", schema = "public")
public class ERole {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  protected UUID id;

  @Column(name = "role_description")
  private String description;

  @Column(name = "role_code")
  @Enumerated(EnumType.STRING)
  private AccountType code;
}
