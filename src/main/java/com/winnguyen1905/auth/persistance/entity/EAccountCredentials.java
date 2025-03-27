package com.winnguyen1905.auth.persistance.entity;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.winnguyen1905.auth.common.constant.AccountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "accounts", schema = "public")
public class EAccountCredentials {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  protected UUID id;

  @Column(name = "account_name")
  private String name;

  @Column(name = "account_username")
  private String username;

  @Column(name = "account_password")
  private String password;

  @Column(name = "account_status")
  private Boolean status;

  @Column(name = "account_email")
  private String email;

  @Column(name = "account_phone")
  private String phone;

  @Column(name = "account_type")
  @Enumerated(EnumType.STRING)
  private AccountType accountType;

  @Column(name = "account_refresh_token")
  private String refreshToken;

  @ManyToOne
  @JoinColumn(name = "role_id")
  private ERole role;
}
