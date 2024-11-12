package com.winnguyen1905.gateway.persistance.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = EShop.class, name = "shop"),
    @JsonSubTypes.Type(value = ECustomer.class, name = "customer"),
})
public class EUserCredentials extends EBaseAudit {

  @Column(name = "frist_name", nullable = true)
  private String firstName;

  @Column(name = "last_name", nullable = true)
  private String lastName;

  @Column(name = "username", unique = true, nullable = false)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "status", nullable = false)
  private Boolean status;

  @Column(name = "email", nullable = true)
  private String email;

  @Column(name = "phone", nullable = true)
  private String phone;

  @Column(name = "type", insertable = false, updatable = false)
  private String type;

  @Column(columnDefinition = "MEDIUMTEXT", name = "refresh_token", nullable = true)
  private String refreshToken;

  // @OneToMany(mappedBy = "user")
  // private List<CommentEntity> MyComments = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "role_id")
  private ERole role;

  // @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
  // private List<OrderEntity> orders;

  // @OneToMany(mappedBy = "sender")
  // private List<NotificationEntity> sents;

  // @OneToMany(mappedBy = "receiver")
  // private List<NotificationEntity> receiveds;

  // @PrePersist
  // protected void prePersist() {
  // this.status = true;
  // }
}
