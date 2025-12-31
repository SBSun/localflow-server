package com.localflow.workflow.domain;

import com.fasterxml.uuid.Generators;
import com.localflow.common.entities.BaseTimeEntity;
import com.localflow.workflow.domain.config.WorkflowConfig;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Workflow extends BaseTimeEntity {

  @Id
  private final UUID id = Generators.timeBasedEpochGenerator().generate();

  @Column(nullable = false)
  private String name;

  private String description;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private WorkflowConfig config;

  @Column(name = "is_active", nullable = false)
  private boolean isActive = true;

  public Workflow(String name, String description) {
    this.name = name;
    this.description = description;
  }
}
