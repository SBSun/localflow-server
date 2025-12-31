package com.localflow.workflow.repository;

import com.localflow.workflow.domain.execution.WorkflowExecution;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowExecutionRepository extends JpaRepository<WorkflowExecution, Long> {
}
