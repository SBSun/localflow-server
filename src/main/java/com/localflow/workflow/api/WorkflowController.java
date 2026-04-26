package com.localflow.workflow.api;

import com.localflow.workflow.api.request.WorkflowCreateRequest;
import com.localflow.workflow.api.request.WorkflowSaveRequest;
import com.localflow.workflow.application.execution.WorkflowExecutionService;
import com.localflow.workflow.application.service.WorkflowService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workflows")
public class WorkflowController {

  private final WorkflowService workflowService;
  private final WorkflowExecutionService workflowExecutionService;

  @PostMapping
  public UUID create(@RequestBody WorkflowCreateRequest request) {
    return workflowService.create(request);
  }

  @PutMapping("/{workflowId}")
  public void save(@PathVariable UUID workflowId, @RequestBody WorkflowSaveRequest request) {
    workflowService.save(workflowId, request);
  }

  @PostMapping("/{workflowId}/execute")
  public void execute(@PathVariable UUID workflowId) {
    workflowExecutionService.startManual(workflowId);
  }
}
