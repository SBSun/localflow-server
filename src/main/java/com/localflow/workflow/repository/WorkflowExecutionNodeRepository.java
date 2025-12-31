package com.localflow.workflow.repository;

import com.localflow.workflow.domain.execution.WorkflowExecutionNode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkflowExecutionNodeRepository extends JpaRepository<WorkflowExecutionNode, UUID> {
}
