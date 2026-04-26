package com.localflow.workflow.execution.node;

import com.localflow.workflow.application.execution.NodeExecutorRegistry;
import com.localflow.workflow.domain.WorkflowNode;
import com.localflow.workflow.domain.execution.WorkflowExecutionNode;
import com.localflow.workflow.execution.context.WorkflowExecutionContext;
import com.localflow.workflow.execution.credential.NodeCredentialProvider;
import com.localflow.workflow.infra.executor.NodeExecutor;
import com.localflow.workflow.repository.WorkflowExecutionNodeRepository;
import com.localflow.workflow.repository.WorkflowNodeRepository;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NodeExecutionService {

  private final NodeExecutorRegistry executorRegistry;
  private final WorkflowNodeRepository workflowNodeRepository;
  private final WorkflowExecutionNodeRepository executionNodeRepository;

  private final NodeCredentialProvider nodeCredentialProvider;

  @Transactional
  public NodeExecutionResult execute(ExecutionNode node, WorkflowExecutionContext executionContext) {
    // 1️⃣ 실행 이력 생성 (RUNNING)
    WorkflowExecutionNode executionNode =
        new WorkflowExecutionNode(executionContext.getExecutionId(), node.getNodeId());

    executionNodeRepository.save(executionNode);

    // 2️⃣ WorkflowNode 조회 (설정 정보 필요)
    WorkflowNode workflowNode = workflowNodeRepository.findById(executionNode.getNodeId())
        .orElseThrow();

    // 3️⃣ NodeExecutionContext 구성
    NodeExecutionContext nodeContext =
        NodeExecutionContext.builder()
            .executionId(executionContext.getExecutionId())
            .workflowId(executionContext.getWorkflowId())
            .workflowNodeId(node.getNodeId())
            .parameters(node.getParameters())
            .credentialProvider(nodeCredentialProvider)
            .inputs(executionContext.getSerializableNodeOutputs())
            .triggerPayload(executionContext.getTriggerPayload())
            .build();

    // 2️⃣ Executor 조회
    NodeExecutor executor =
        executorRegistry.get(node.getNodeKey());

    try {
      NodeExecutionResult result = executor.execute(nodeContext);

      if (result == null) {
        throw new IllegalStateException("NodeExecutor returned null: " + node.getNodeKey());
      }

      executionNode.success(result.outputData());
      return result;
    } catch (Exception e) {
      executionNode.fail(e);
      return NodeExecutionResult.failure(e);
    }
  }
}
