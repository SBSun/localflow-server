package com.localflow.workflow.infra.executor;

import com.localflow.workflow.execution.node.NodeExecutionContext;
import com.localflow.workflow.execution.node.NodeExecutionResult;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WebhookExecutor implements NodeExecutor {

  @Override
  public NodeExecutionResult execute(NodeExecutionContext context) throws Exception {
    return NodeExecutionResult.success(Map.of(
        "payload", context.getTriggerPayload()
    ));
  }

  @Override
  public String getNodeKey() {
    return "webhook.trigger";
  }
}
