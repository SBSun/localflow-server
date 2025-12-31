package com.localflow.workflow.domain;

import com.fasterxml.uuid.Generators;
import com.localflow.common.entities.BaseTimeEntity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "workflow_connection")
public class WorkflowConnection extends BaseTimeEntity {

  @Id
  private final UUID id = Generators.timeBasedEpochGenerator().generate();

  @Column(name = "workflow_id", nullable = false)
  private UUID workflowId;

  @Column(name = "from_node_id", nullable = false)
  private UUID fromNodeId;

  @Column(name = "to_node_id", nullable = false)
  private UUID toNodeId;

  public WorkflowConnection(UUID workflowId, UUID fromNodeId, UUID toNodeId) {
    this.workflowId = workflowId;
    this.fromNodeId = fromNodeId;
    this.toNodeId = toNodeId;
  }
}
