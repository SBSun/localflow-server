package com.localflow.workflow.execution.graph;

import com.localflow.workflow.application.execution.NodeExecutorRegistry;
import com.localflow.workflow.domain.WorkflowConnection;
import com.localflow.workflow.domain.WorkflowNode;
import com.localflow.workflow.execution.node.ExecutionNode;
import com.localflow.workflow.infra.executor.NodeExecutor;
import com.localflow.workflow.repository.WorkflowConnectionRepository;
import com.localflow.workflow.repository.WorkflowNodeRepository;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExecutionGraphBuilder {

  private final WorkflowNodeRepository nodeRepository;
  private final WorkflowConnectionRepository connectionRepository;
  private final NodeExecutorRegistry executorRegistry;

  public ExecutionGraph build(UUID workflowId, UUID startNodeId) {
    List<WorkflowNode> workflowNodes =
        nodeRepository.findAllByWorkflowId(workflowId);

    List<WorkflowConnection> nodeConnections =
        connectionRepository.findAllByWorkflowId(workflowId);

    Map<UUID, ExecutionNode> nodeMap = new HashMap<>();

    // 1. ExecutionNode 생성
    for (WorkflowNode node : workflowNodes) {
      NodeExecutor executor =
          executorRegistry.get(node.getNodeKey());

      ExecutionNode execNode = new ExecutionNode(
          node.getId(),
          node.getNodeKey(),
          node.getName(),
          executor,
          node.getParameters()
      );

      nodeMap.put(execNode.getNodeId(), execNode);
    }

    for (WorkflowConnection connection : nodeConnections) {
      ExecutionNode from = nodeMap.get(connection.getFromNodeId());
      ExecutionNode to = nodeMap.get(connection.getToNodeId());

      if (from != null && to != null) {
        from.addNext(to);
      }
    }

    ExecutionNode startNode = nodeMap.get(startNodeId);
    if (startNode == null) {
      throw new IllegalStateException("Start node not found in workflow: " + startNodeId);
    }
    if (!isTriggerNode(startNode)) {
      throw new IllegalStateException("Workflow execution must start from a trigger node: " + startNodeId);
    }

    return new ExecutionGraph(nodeMap, List.of(startNode));
  }

  private boolean isTriggerNode(ExecutionNode node) {
    return node.getNodeKey().endsWith(".trigger");
  }
}
