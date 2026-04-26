package com.localflow.workflow.application.execution;

import com.localflow.workflow.application.trigger.TriggerContext;
import com.localflow.workflow.application.trigger.TriggerType;
import com.localflow.workflow.domain.Workflow;
import com.localflow.workflow.domain.WorkflowNode;
import com.localflow.workflow.domain.execution.WorkflowExecution;
import com.localflow.workflow.repository.WorkflowExecutionRepository;
import com.localflow.workflow.repository.WorkflowNodeRepository;
import com.localflow.workflow.repository.WorkflowRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkflowExecutionService {

  private static final String MANUAL_TRIGGER_NODE_KEY = "manual.trigger";

  private final WorkflowRepository workflowRepository;
  private final WorkflowNodeRepository workflowNodeRepository;
  private final WorkflowExecutionRepository executionRepository;
  private final WorkflowExecutionRunner runner;

  @Transactional
  public void startManual(UUID workflowId) {
    WorkflowNode manualTriggerNode = getManualTriggerNode(workflowId);
    TriggerContext context = new TriggerContext(
        TriggerType.MANUAL,
        manualTriggerNode.getId().toString(),
        java.util.Map.of()
    );

    start(workflowId, context, manualTriggerNode.getId());
  }

  @Transactional
  public void start(UUID workflowId, TriggerContext triggerContext, UUID startNodeId) {
    Workflow workflow = workflowRepository.findById(workflowId)
        .orElseThrow(() -> new IllegalArgumentException("Workflow not found"));

    if (!workflow.isActive()) {
      throw new IllegalStateException("Workflow is inactive");
    }

    WorkflowExecution execution = new WorkflowExecution(workflow.getId(), triggerContext);

    executionRepository.save(execution);

    // 동기 실행
    runner.run(execution.getId(), startNodeId);
  }

  private WorkflowNode getManualTriggerNode(UUID workflowId) {
    java.util.List<WorkflowNode> triggerNodes =
        workflowNodeRepository.findAllByWorkflowIdAndNodeKey(workflowId, MANUAL_TRIGGER_NODE_KEY);

    if (triggerNodes.isEmpty()) {
      throw new IllegalStateException("Manual trigger node not found");
    }
    if (triggerNodes.size() > 1) {
      throw new IllegalStateException("Multiple manual trigger nodes found");
    }

    return triggerNodes.getFirst();
  }
}
