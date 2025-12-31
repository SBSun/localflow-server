package com.localflow.workflow.execution.node;

import com.localflow.workflow.infra.executor.NodeExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;

@Getter
public class ExecutionNode {

  private final UUID nodeId;             // workflow_node.id
  private final String nodeKey;          // webhook, notion.createPage
  private final String name;
  private final NodeExecutor executor;
  private final Map<String, Object> parameters;
  private final List<ExecutionNode> nextNodes = new ArrayList<>();

  public ExecutionNode(
      UUID nodeId,
      String nodeKey,
      String name,
      NodeExecutor executor,
      Map<String, Object> parameters
  ) {
    this.nodeId = nodeId;
    this.nodeKey = nodeKey;
    this.name = name;
    this.executor = executor;
    this.parameters = parameters;
  }

  public void addNext(ExecutionNode next) {
    this.nextNodes.add(next);
  }
}

