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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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

    // 2 & 3. 연결 설정 및 대상 ID 수집을 동시에 처리
    Set<UUID> targetIds = new HashSet<>();

    for (WorkflowConnection connection : nodeConnections) {
      ExecutionNode from = nodeMap.get(connection.getFromNodeId());
      ExecutionNode to = nodeMap.get(connection.getToNodeId());

      if (from != null && to != null) {
        from.addNext(to);
        targetIds.add(to.getNodeId());
      }
    }

    // 3. 시작 노드 필터링
    List<ExecutionNode> startNodes = nodeMap.values().stream()
        .filter(n -> !targetIds.contains(n.getNodeId()))
        .toList();
    
    if (startNodes.isEmpty() && !nodeMap.isEmpty()) {
      throw new IllegalStateException("Circular dependency detected or no entry point found.");
    }

    return new ExecutionGraph(nodeMap, startNodes);
  }
}

