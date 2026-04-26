package com.localflow.workflow.execution.node;

import com.localflow.workflow.execution.credential.NodeCredentialProvider;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NodeExecutionContext {

  private final Long executionId;
  private final UUID workflowId;
  private final UUID workflowNodeId;
  private final Map<String, Object> parameters;
  private final NodeCredentialProvider credentialProvider;
  private final Map<String, Object> inputs;
  private final Map<String, Object> triggerPayload;
  private final LocalDateTime startedAt = LocalDateTime.now();
}
