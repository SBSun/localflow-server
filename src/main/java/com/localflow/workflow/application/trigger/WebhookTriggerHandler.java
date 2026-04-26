package com.localflow.workflow.application.trigger;

import com.localflow.workflow.application.execution.WorkflowExecutionService;
import com.localflow.workflow.domain.WorkflowNode;
import com.localflow.workflow.repository.WorkflowNodeRepository;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebhookTriggerHandler implements TriggerHandler {

  private final WorkflowNodeRepository workflowNodeRepository;
  private final WorkflowExecutionService executionService;

  @Override
  public TriggerType getType() {
    return TriggerType.WEBHOOK;
  }

  @Override
  public void handle(TriggerContext context) {
    WorkflowNode webhookNode = workflowNodeRepository.findWebhookNode(context.triggerKey())
        .orElseThrow(() -> new IllegalArgumentException("Webhook not found"));

    executionService.start(
        webhookNode.getWorkflowId(),
        context,
        webhookNode.getId()
    );
  }
}
