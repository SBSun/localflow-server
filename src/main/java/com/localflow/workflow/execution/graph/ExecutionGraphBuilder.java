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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExecutionGraphBuilder {

  private final WorkflowNodeRepository nodeRepository;
  private final WorkflowConnectionRepository connectionRepository;
  private final NodeExecutorRegistry executorRegistry;

  public ExecutionGraph build(UUID workflowId) {
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

    // 2. 연결 (DAG)
    for (WorkflowConnection connection : nodeConnections) {
      ExecutionNode from = nodeMap.get(connection.getFromNodeId().toString());
      ExecutionNode to = nodeMap.get(connection.getToNodeId().toString());

      from.addNext(to);
    }

    // 3. 시작 노드 찾기
    Set<String> targetIds = nodeConnections.stream()
        .map(c -> c.getToNodeId().toString())
        .collect(Collectors.toSet());

    List<ExecutionNode> startNodes = nodeMap.values().stream()
        .filter(n -> !targetIds.contains(n.getNodeId()))
        .toList();

    return new ExecutionGraph(nodeMap, startNodes);
  }
}

