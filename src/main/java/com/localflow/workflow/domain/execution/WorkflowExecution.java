package com.localflow.workflow.domain.execution;

import com.localflow.workflow.application.trigger.TriggerContext;
import com.localflow.workflow.domain.execution.enums.WorkflowExecutionStatus;

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
@Table(name = "workflow_execution")
public class WorkflowExecution {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "workflow_id", nullable = false)
  private UUID workflowId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private WorkflowExecutionStatus status;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "trigger_context", columnDefinition = "jsonb")
  private TriggerContext triggerContext;

  @Column(name = "started_at", nullable = false)
  private LocalDateTime startedAt;

  @Column(name = "finished_at")
  private LocalDateTime finishedAt;

  public WorkflowExecution(UUID workflowId, TriggerContext triggerContext) {
    this.workflowId = workflowId;
    this.status = WorkflowExecutionStatus.RUNNING;
    this.triggerContext = triggerContext;
    this.startedAt = LocalDateTime.now();
  }

  public void complete() {
    this.status = WorkflowExecutionStatus.SUCCESS;
    this.finishedAt = LocalDateTime.now();
  }

  public void fail() {
    this.status = WorkflowExecutionStatus.FAILED;
    this.finishedAt = LocalDateTime.now();
  }
}
