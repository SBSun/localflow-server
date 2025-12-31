package com.localflow.workflow.application.execution;

import com.localflow.workflow.domain.execution.WorkflowExecution;
import com.localflow.workflow.execution.context.WorkflowExecutionContext;
import com.localflow.workflow.execution.credential.CredentialProvider;
import com.localflow.workflow.execution.credential.NodeCredentialProvider;
import com.localflow.workflow.execution.credential.WorkflowScopedCredentialProvider;
import com.localflow.workflow.execution.engine.ExecutionEngine;
import com.localflow.workflow.execution.graph.ExecutionGraph;
import com.localflow.workflow.execution.graph.ExecutionGraphBuilder;
import com.localflow.workflow.repository.WorkflowExecutionRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WorkflowExecutionRunner {

  private final WorkflowExecutionRepository executionRepository;
  private final ExecutionGraphBuilder graphBuilder;
  private final ExecutionEngine executionEngine;

  private final NodeCredentialProvider baseCredentialProvider;

  @Transactional
  public void run(Long executionId) {
    WorkflowExecution execution = executionRepository.findById(executionId)
        .orElseThrow();

    ExecutionGraph graph = graphBuilder.build(execution.getWorkflowId());

    CredentialProvider credentialProvider =
        new WorkflowScopedCredentialProvider(
            execution.getWorkflowId(),
            baseCredentialProvider
        );

    WorkflowExecutionContext context =
        new WorkflowExecutionContext(execution, credentialProvider);
    try {
      executionEngine.execute(graph, context);
      execution.complete();
    } catch (Exception e) {
      execution.fail();
      throw e;
    }
  }
}
