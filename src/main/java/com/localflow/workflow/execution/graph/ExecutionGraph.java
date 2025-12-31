package com.localflow.workflow.execution.graph;

import com.localflow.workflow.execution.node.ExecutionNode;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;

@Getter
public class ExecutionGraph {

  private final Map<UUID, ExecutionNode> nodes;
  // Trigger 노드 또는 진입 차수가 0인 노드
  private final List<ExecutionNode> startNodes;

  public ExecutionGraph(
      Map<UUID, ExecutionNode> nodes,
      List<ExecutionNode> startNodes
  ) {
    this.nodes = nodes;
    this.startNodes = startNodes;
  }
}
