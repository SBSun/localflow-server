package com.localflow.workflow.application.execution;

import com.localflow.workflow.application.trigger.TriggerContext;
import com.localflow.workflow.domain.Workflow;
import com.localflow.workflow.domain.execution.WorkflowExecution;
import com.localflow.workflow.repository.WorkflowExecutionRepository;
import com.localflow.workflow.repository.WorkflowRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkflowExecutionService {

  private final WorkflowRepository workflowRepository;
  private final WorkflowExecutionRepository executionRepository;
  private final WorkflowExecutionRunner runner;

  @Transactional
  public void start(UUID workflowId, TriggerContext triggerContext) {
    Workflow workflow = workflowRepository.findById(workflowId)
        .orElseThrow(() -> new IllegalArgumentException("Workflow not found"));

    if (!workflow.isActive()) {
      throw new IllegalStateException("Workflow is inactive");
    }

    WorkflowExecution execution = new WorkflowExecution(workflow.getId(), triggerContext);

    executionRepository.save(execution);

    // 동기 실행
    runner.run(execution.getId());
  }
}
