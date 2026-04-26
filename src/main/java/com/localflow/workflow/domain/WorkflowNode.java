package com.localflow.workflow.domain;

import com.localflow.common.entities.BaseTimeEntity;
import com.localflow.workflow.domain.config.CredentialConfig;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "workflow_node")
public class WorkflowNode extends BaseTimeEntity {

  @Id
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "workflow_id")
  private Workflow workflow;
  
  @Column(name = "workflow_id", insertable = false, updatable = false)
  private UUID workflowId;

  @Column(name = "node_key", nullable = false)
  private String nodeKey;

  @Column(nullable = false)
  private String name;

  private String version;

  @Column(name = "position_x", nullable = false)
  private Integer positionX;

  @Column(name = "position_y", nullable = false)
  private Integer positionY;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> parameters;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "credentials", columnDefinition = "jsonb")
  private List<CredentialConfig> credentials;

  @Builder
  public WorkflowNode(
      UUID id,
      String nodeKey,
      String name,
      String version,
      Integer positionX,
      Integer positionY,
      Map<String, Object> parameters,
      List<CredentialConfig> credentials
  ) {
    this.id = id;
    this.nodeKey = nodeKey;
    this.name = name;
    this.version = version;
    this.positionX = positionX;
    this.positionY = positionY;
    this.parameters = parameters;
    this.credentials = credentials;
  }

  public void setWorkflow(Workflow workflow) {
    this.workflow = workflow;
  }

  public void update(String nodeKey, String name, String version,
      Integer positionX, Integer positionY,
      Map<String, Object> parameters,
      List<CredentialConfig> credentials) {
    this.nodeKey = nodeKey;
    this.name = name;
    this.version = version;
    this.positionX = positionX;
    this.positionY = positionY;
    this.parameters = parameters;
    this.credentials = credentials;
  }
}
