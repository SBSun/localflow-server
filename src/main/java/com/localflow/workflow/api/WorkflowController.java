package com.localflow.workflow.api;

import com.localflow.workflow.api.request.WorkflowCreateRequest;
import com.localflow.workflow.service.WorkflowService;

import org.springframework.web.bind.annotation.PostMapping;
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

  @PostMapping
  public UUID create(@RequestBody WorkflowCreateRequest request) {
    return workflowService.create(request);
  }
}
