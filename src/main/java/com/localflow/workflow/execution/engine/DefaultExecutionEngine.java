package com.localflow.workflow.execution.engine;

import com.localflow.workflow.domain.execution.enums.NodeExecutionStatus;
import com.localflow.workflow.execution.context.WorkflowExecutionContext;
import com.localflow.workflow.execution.graph.ExecutionGraph;
import com.localflow.workflow.execution.node.ExecutionNode;
import com.localflow.workflow.execution.node.NodeExecutionResult;
import com.localflow.workflow.execution.node.NodeExecutionService;

import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultExecutionEngine implements ExecutionEngine {

  private final NodeExecutionService nodeExecutionService;

  @Override
  public void execute(ExecutionGraph graph, WorkflowExecutionContext context) {
    Deque<ExecutionNode> readyQueue = new ArrayDeque<>();

    readyQueue.addAll(graph.getStartNodes());

    while (!readyQueue.isEmpty()) {
      ExecutionNode currentNode = readyQueue.poll();

      NodeExecutionResult result = nodeExecutionService.execute(currentNode, context);

      // 실패 시 즉시 중단
      if (result.status() == NodeExecutionStatus.FAILED) {
        throw new IllegalArgumentException(result.errorMessage());
      }

      // 4️⃣ 다음 실행 가능한 노드 결정
      List<ExecutionNode> nextNodes = currentNode.getNextNodes();

      readyQueue.addAll(nextNodes);
    }
  }
}
