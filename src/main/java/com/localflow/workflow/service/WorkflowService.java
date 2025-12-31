package com.localflow.workflow.service;

import com.localflow.workflow.api.request.WorkflowCreateRequest;
import com.localflow.workflow.domain.Workflow;
import com.localflow.workflow.repository.WorkflowRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkflowService {

  private final WorkflowRepository workflowRepository;

  @Transactional
  public UUID create(WorkflowCreateRequest request) {
    Workflow newWorkflow = new Workflow(request.name(), request.description());

    workflowRepository.save(newWorkflow);
    return newWorkflow.getId();
  }
}
