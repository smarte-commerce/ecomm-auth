package com.winnguyen1905.auth.persistance.entity;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.winnguyen1905.auth.common.constant.AccountType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts", schema = "public")
public class EAccountCredentials {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  protected UUID id;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "status")
  private Boolean status;

  @Column(name = "role_code")
  @Enumerated(EnumType.STRING)
  private AccountType accountType;

  @Column(name = "last_token", length = 1024)
  private String refreshToken;

  @OneToOne(mappedBy = "accountCredentials", cascade = CascadeType.ALL)
  private ECustomer customer;

  @OneToOne(mappedBy = "accountCredentials", cascade = CascadeType.ALL)
  private EVendor vendor;

  @PrePersist
  public void prePersist() {
    if (this.id == null) {
      this.id = UUID.randomUUID();
    }
  }
}
