package com.localflow.workflow.infra.executor;

import com.localflow.workflow.execution.node.NodeExecutionContext;
import com.localflow.workflow.execution.node.NodeExecutionResult;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogExecutor implements NodeExecutor {

  @Override
  public NodeExecutionResult execute(NodeExecutionContext context) {
    Map<String, Object> parameters = context.getParameters() == null
        ? Collections.emptyMap()
        : context.getParameters();

    Map<String, Object> output = new LinkedHashMap<>();
    output.put("message", parameters.getOrDefault("message", "Log node executed"));
    output.put("executedAt", LocalDateTime.now().toString());
    output.put("executionId", context.getExecutionId());
    output.put("workflowId", context.getWorkflowId().toString());
    output.put("workflowNodeId", context.getWorkflowNodeId().toString());
    output.put("parameters", parameters);
    output.put("triggerPayload", context.getTriggerPayload());
    output.put("inputs", context.getInputs());

    log.info(
        "Log node executed. executionId={}, workflowId={}, nodeId={}, message={}, parameters={}, triggerPayload={}, inputs={}",
        context.getExecutionId(),
        context.getWorkflowId(),
        context.getWorkflowNodeId(),
        output.get("message"),
        parameters,
        context.getTriggerPayload(),
        context.getInputs()
    );

    return NodeExecutionResult.success(output);
  }

  @Override
  public String getNodeKey() {
    return "debug.log";
  }
}
