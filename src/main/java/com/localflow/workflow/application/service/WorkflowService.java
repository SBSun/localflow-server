package com.localflow.workflow.application.service;

import com.localflow.workflow.api.request.WorkflowCreateRequest;
import com.localflow.workflow.api.request.WorkflowNodeRequest;
import com.localflow.workflow.api.request.WorkflowSaveRequest;
import com.localflow.workflow.domain.Workflow;
import com.localflow.workflow.domain.WorkflowNode;
import com.localflow.workflow.repository.WorkflowRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
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

  @Transactional
  public void save(UUID workflowId, WorkflowSaveRequest request) {
    Workflow workflow = getWorkflow(workflowId);
    workflow.update(request.name(), request.description(), request.active());

    List<WorkflowNode> existingNodes = workflow.getNodes();
    List<WorkflowNodeRequest> requestedNodes = request.nodes();

    // 요청 목록에 없는 노드는 삭제
    existingNodes.removeIf(node ->
        requestedNodes.stream()
            .noneMatch(req -> req.id() != null && req.id().equals(node.getId()))
    );

    for (WorkflowNodeRequest nodeReq : requestedNodes) {
      if (nodeReq.id() != null) {
        existingNodes.stream()
            .filter(node -> node.getId().equals(nodeReq.id()))
            .findFirst()
            .ifPresent(node -> node.update(
                nodeReq.nodeKey(),
                nodeReq.name(),
                nodeReq.version(),
                nodeReq.position().x(),
                nodeReq.position().y(),
                nodeReq.parameters()
            ));
      } else {
        WorkflowNode newNode = WorkflowNode.builder()
            .nodeKey(nodeReq.nodeKey())
            .name(nodeReq.name())
            .version(nodeReq.version())
            .positionX(nodeReq.position().x())
            .positionY(nodeReq.position().y())
            .parameters(nodeReq.parameters())
            .build();

        workflow.addNode(newNode);
      }
    }
  }

  public Workflow getWorkflow(UUID workflowId) {
    return workflowRepository.findById(workflowId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 workflow입니다."));
  }
}
