package com.winnguyen1905.gateway.persistance.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customers")
public class ECustomer extends EUser {
    // @OneToMany(mappedBy = "customer")
    // Set<ReservationEntity> reservations = new HashSet<>();
}