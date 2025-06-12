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
@Table(name = "vendors", schema = "public")
public class EVendor {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @OneToOne
  @MapsId
  @JoinColumn(name = "id")
  private EAccountCredentials accountCredentials;

  @Column(name = "vendor_name")
  private String vendorName;

  @Column(name = "vendor_description")
  private String vendorDescription;

  @Column(name = "vendor_address")
  private String vendorAddress;

  @Column(name = "vendor_phone")
  private String vendorPhone;

  @Column(name = "vendor_email")
  private String vendorEmail;

  @Column(name = "vendor_logo")
  private String vendorLogo;

  @Column(name = "vendor_status")
  private Boolean vendorStatus;
}
