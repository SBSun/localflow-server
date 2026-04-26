package com.localflow.workflow.repository;

import com.localflow.workflow.domain.execution.WorkflowExecutionNode;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowExecutionNodeRepository extends JpaRepository<WorkflowExecutionNode, Long> {
}
