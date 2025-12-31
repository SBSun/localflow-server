package com.localflow.workflow.infra.executor;

import com.localflow.workflow.execution.node.NodeExecutionContext;
import com.localflow.workflow.execution.node.NodeExecutionResult;

public interface NodeExecutor {

  NodeExecutionResult execute(NodeExecutionContext context) throws Exception;

  String getNodeKey();
}
