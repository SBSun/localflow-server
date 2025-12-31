package com.localflow.workflow.domain.execution;

import com.localflow.workflow.domain.execution.enums.NodeExecutionStatus;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "workflow_execution_node")
public class WorkflowExecutionNode {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "workflow_execution_id", nullable = false)
  private Long workflowExecutionId;

  @Column(name = "node_id", nullable = false)
  private UUID nodeId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NodeExecutionStatus status;

  @Column(name = "started_at", nullable = false)
  private LocalDateTime startedAt;

  @Column(name = "finished_at")
  private LocalDateTime finishedAt;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "input_data", columnDefinition = "jsonb")
  private Object inputData;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "output_data", columnDefinition = "jsonb")
  private Object outputData;

  @Column(name = "error_message", columnDefinition = "text")
  private String errorMessage;

  public WorkflowExecutionNode(Long workflowExecutionId, UUID nodeId) {
    this.workflowExecutionId = workflowExecutionId;
    this.nodeId = nodeId;
    this.status = NodeExecutionStatus.RUNNING;
    this.startedAt = LocalDateTime.now();
  }

  public void fail(Exception e) {
    this.status = NodeExecutionStatus.FAILED;
    this.errorMessage = e.getMessage();
    this.finishedAt = LocalDateTime.now();
  }

  public void success(Object outputData) {
    this.status = NodeExecutionStatus.SUCCESS;
    this.outputData = outputData;
    this.finishedAt = LocalDateTime.now();
  }
}
