package com.winnguyen1905.gateway.persistance.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set; 

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_detail", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<EPermission> permissions = new HashSet<>();

    @OneToMany(mappedBy = "role")
    private Set<EUser> users = new HashSet<>();
}