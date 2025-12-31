package com.localflow.workflow.infra.executor;

import com.localflow.workflow.execution.node.NodeExecutionContext;
import com.localflow.workflow.execution.node.NodeExecutionResult;

import org.springframework.stereotype.Component;

@Component
public class WebhookExecutor implements NodeExecutor {

  @Override
  public NodeExecutionResult execute(NodeExecutionContext context) throws Exception {
    return null;
  }

  @Override
  public String getNodeKey() {
    return "webhook.trigger";
  }
}
