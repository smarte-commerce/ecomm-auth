package com.winnguyen1905.auth.persistance.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
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
@Table(name = "customers", schema = "public")
public class ECustomer {
  @OneToOne
  @MapsId
  @JoinColumn(name = "id")
  private EAccountCredentials accountCredentials;

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "customer_name")
  private String customerName;

  @Column(name = "customer_address")
  private String customerAddress;

  @Column(name = "customer_phone")
  private String customerPhone;

  @Column(name = "customer_email")
  private String customerEmail;

  @Column(name = "customer_logo")
  private String customerLogo;

  @Column(name = "customer_status")
  private Boolean customerStatus;
}
