package com.localflow.workflow.execution.engine;

import com.localflow.workflow.execution.context.WorkflowExecutionContext;
import com.localflow.workflow.execution.graph.ExecutionGraph;

public interface ExecutionEngine {

  void execute(ExecutionGraph graph, WorkflowExecutionContext context);
}
