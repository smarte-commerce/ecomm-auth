package com.winnguyen1905.gateway.persistence.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.*;
import lombok.*;

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
public class EUser extends EBaseAudit {

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

    // @ManyToOne
    // @JoinColumn(name = "role_id")
    // private RoleEntity role;

    // @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    // private List<OrderEntity> orders;

    // @OneToMany(mappedBy = "sender")
    // private List<NotificationEntity> sents;

    // @OneToMany(mappedBy = "receiver")
    // private List<NotificationEntity> receiveds;

    // @PrePersist
    // protected void prePersist() {
    //     this.status = true;
    // }
}