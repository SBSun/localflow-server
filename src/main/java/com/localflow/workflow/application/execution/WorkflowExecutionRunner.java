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

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkflowExecutionRunner {

  private final WorkflowExecutionRepository executionRepository;
  private final ExecutionGraphBuilder graphBuilder;
  private final ExecutionEngine executionEngine;

  private final NodeCredentialProvider baseCredentialProvider;

  @Transactional
  public void run(Long executionId, UUID startNodeId) {
    WorkflowExecution execution = executionRepository.findById(executionId)
        .orElseThrow();

    ExecutionGraph graph = graphBuilder.build(execution.getWorkflowId(), startNodeId);

    CredentialProvider credentialProvider =
        new WorkflowScopedCredentialProvider(
            execution.getWorkflowId(),
            baseCredentialProvider
        );

    WorkflowExecutionContext context = new WorkflowExecutionContext(execution, credentialProvider);
    try {
      executionEngine.execute(graph, context);
      execution.complete();
    } catch (Exception e) {
      log.error(
          "Workflow execution failed. executionId={}, workflowId={}, startNodeId={}",
          executionId,
          execution.getWorkflowId(),
          startNodeId,
          e
      );
      execution.fail();
    }
  }
}
