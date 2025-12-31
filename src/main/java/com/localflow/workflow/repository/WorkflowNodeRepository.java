package com.localflow.workflow.repository;

import com.localflow.workflow.domain.WorkflowNode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkflowNodeRepository extends JpaRepository<WorkflowNode, UUID> {

  List<WorkflowNode> findAllByWorkflowId(UUID workflowId);

  @Query(
      value = """
          select *
          from workflow_node wn
          where wn.node_key = 'webhook'
            and wn.parameters ->> 'webhookKey' = :webhookKey
            and wn.disabled = false
          """,
      nativeQuery = true
  )
  Optional<WorkflowNode> findWebhookNode(String webhookKey);
}

