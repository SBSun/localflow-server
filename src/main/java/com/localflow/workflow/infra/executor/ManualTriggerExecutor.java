package com.localflow.workflow.infra.executor;

import com.localflow.workflow.execution.node.NodeExecutionContext;
import com.localflow.workflow.execution.node.NodeExecutionResult;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ManualTriggerExecutor implements NodeExecutor {

  @Override
  public NodeExecutionResult execute(NodeExecutionContext context) {
    Map<String, Object> output = new LinkedHashMap<>();
    output.put("triggerType", "MANUAL");
    output.put("executedAt", LocalDateTime.now().toString());
    output.put("executionId", context.getExecutionId());
    output.put("workflowId", context.getWorkflowId().toString());
    output.put("workflowNodeId", context.getWorkflowNodeId().toString());
    output.put("payload", context.getTriggerPayload());

    log.info(
        "Manual trigger executed. executionId={}, workflowId={}, nodeId={}",
        context.getExecutionId(),
        context.getWorkflowId(),
        context.getWorkflowNodeId()
    );

    return NodeExecutionResult.success(output);
  }

  @Override
  public String getNodeKey() {
    return "manual.trigger";
  }
}
