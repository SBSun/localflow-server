package com.localflow.workflow.execution.node;

import com.localflow.workflow.domain.execution.enums.NodeExecutionStatus;

import java.util.Map;

public record NodeExecutionResult(
    NodeExecutionStatus status,
    Map<String, Object> outputData,
    String errorMessage
) {

  public static NodeExecutionResult failure(Exception e) {
    return new NodeExecutionResult(
        NodeExecutionStatus.FAILED,
        null,
        e.getMessage()
    );
  }

  public static NodeExecutionResult success(Map<String, Object> output) {
    return new NodeExecutionResult(
        NodeExecutionStatus.SUCCESS,
        output,
        null
    );
  }
}
