package com.localflow.workflow.execution.context;

import com.localflow.workflow.domain.execution.WorkflowExecution;
import com.localflow.workflow.execution.credential.CredentialProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;

@Getter
public class WorkflowExecutionContext {

  private final Long executionId;
  private final UUID workflowId;
  private final Map<UUID, Object> nodeOutputs = new HashMap<>();
  private final Map<String, Object> triggerPayload;
  private final CredentialProvider credentialProvider;

  public WorkflowExecutionContext(
      WorkflowExecution execution,
      CredentialProvider credentialProvider
  ) {
    this.executionId = execution.getId();
    this.workflowId = execution.getWorkflowId();
    this.triggerPayload = execution.getTriggerContext().payload();
    this.credentialProvider = credentialProvider;
  }

  public Object getOutput(UUID nodeId) {
    return nodeOutputs.get(nodeId);
  }

  public void putOutput(UUID nodeId, Object output) {
    nodeOutputs.put(nodeId, output);
  }
}