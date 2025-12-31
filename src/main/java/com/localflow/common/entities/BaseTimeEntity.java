package com.localflow.common.entities;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity implements Serializable {

  @Column(name = "created_at", updatable = false, nullable = false)
  protected LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  protected LocalDateTime updatedAt;

  @PrePersist
  protected void onPrePersist() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = this.createdAt;
  }

  @PreUpdate
  protected void onPreUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}