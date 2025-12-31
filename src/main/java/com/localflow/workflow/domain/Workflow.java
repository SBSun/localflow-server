package com.localflow.workflow.domain;

import com.fasterxml.uuid.Generators;
import com.localflow.common.entities.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

  @Column(name = "is_active", nullable = false)
  private boolean isActive = true;

  @OneToMany(mappedBy = "workflow", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<WorkflowNode> nodes = new ArrayList<>();

  public Workflow(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public void addNode(WorkflowNode node) {
    this.nodes.add(node);
    node.setWorkflow(this);
  }

  public void update(String name, String description, boolean isActive) {
    this.name = name;
    this.description = description;
    this.isActive = isActive;
  }
}
