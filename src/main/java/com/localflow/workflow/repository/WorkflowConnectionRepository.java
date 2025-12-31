package com.localflow.workflow.repository;

import com.localflow.workflow.domain.WorkflowConnection;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkflowConnectionRepository extends JpaRepository<WorkflowConnection, UUID> {

  List<WorkflowConnection> findAllByWorkflowId(UUID workflowId);
}
