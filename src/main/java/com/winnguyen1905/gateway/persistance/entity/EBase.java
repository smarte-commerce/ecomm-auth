package com.winnguyen1905.gateway.persistance.entity;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
public abstract class EBase implements Serializable {
  private static final long serialVersionUID = -863164858986274318L;

  @Id
  @Column(columnDefinition = "BINARY(16)")
  @GeneratedValue(generator = "custom-uuid")
  private UUID id;

  @Column(name = "is_deleted", updatable = true)
  private Boolean isDeleted;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof EBase))
      return false;
    EBase that = (EBase) o;
    return id.equals(that.id);
  }
}
