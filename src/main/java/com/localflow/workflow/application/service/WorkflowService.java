package com.localflow.workflow.application.service;

import com.fasterxml.uuid.Generators;
import com.localflow.workflow.api.request.WorkflowCreateRequest;
import com.localflow.workflow.api.request.WorkflowConnectionRequest;
import com.localflow.workflow.api.request.WorkflowNodeRequest;
import com.localflow.workflow.api.request.WorkflowSaveRequest;
import com.localflow.workflow.application.execution.NodeExecutorRegistry;
import com.localflow.workflow.domain.Workflow;
import com.localflow.workflow.domain.WorkflowConnection;
import com.localflow.workflow.domain.WorkflowNode;
import com.localflow.workflow.domain.config.CredentialConfig;
import com.localflow.workflow.domain.credential.Credential;
import com.localflow.workflow.domain.credential.enums.CredentialType;
import com.localflow.workflow.repository.CredentialRepository;
import com.localflow.workflow.repository.WorkflowConnectionRepository;
import com.localflow.workflow.repository.WorkflowNodeRepository;
import com.localflow.workflow.repository.WorkflowRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkflowService {

  private final WorkflowRepository workflowRepository;
  private final WorkflowNodeRepository workflowNodeRepository;
  private final WorkflowConnectionRepository workflowConnectionRepository;
  private final CredentialRepository credentialRepository;
  private final NodeExecutorRegistry nodeExecutorRegistry;

  @Transactional
  public UUID create(WorkflowCreateRequest request) {
    Workflow newWorkflow = new Workflow(request.name(), request.description());
    newWorkflow.addNode(createDefaultManualTriggerNode());

    workflowRepository.save(newWorkflow);
    return newWorkflow.getId();
  }

  @Transactional
  public void save(UUID workflowId, WorkflowSaveRequest request) {
    Workflow workflow = getWorkflow(workflowId);
    workflow.update(request.name(), request.description(), request.active());

    List<WorkflowNode> existingNodes = workflow.getNodes();
    List<WorkflowNodeRequest> requestedNodes = request.nodes() == null
        ? Collections.emptyList()
        : request.nodes();

    validateNodeRequests(requestedNodes, existingNodes);

    // 요청 목록에 없는 노드는 삭제
    existingNodes.removeIf(node ->
        requestedNodes.stream()
            .noneMatch(req -> req.id() != null && req.id().equals(node.getId()))
    );

    Map<UUID, WorkflowNode> existingNodeMap = existingNodes.stream()
        .collect(Collectors.toMap(WorkflowNode::getId, Function.identity()));

    for (WorkflowNodeRequest nodeReq : requestedNodes) {
      List<CredentialConfig> credentials = toCredentialConfigs(nodeReq.credentials());

      WorkflowNode existingNode = existingNodeMap.get(nodeReq.id());
      if (existingNode != null) {
        existingNode.update(
            nodeReq.nodeKey(),
            nodeReq.name(),
            nodeReq.version(),
            nodeReq.position().x(),
            nodeReq.position().y(),
            nodeReq.parameters(),
            credentials
        );
      } else {
        WorkflowNode newNode = WorkflowNode.builder()
            .id(nodeReq.id())
            .nodeKey(nodeReq.nodeKey())
            .name(nodeReq.name())
            .version(nodeReq.version())
            .positionX(nodeReq.position().x())
            .positionY(nodeReq.position().y())
            .parameters(nodeReq.parameters())
            .credentials(credentials)
            .build();

        workflow.addNode(newNode);
      }
    }

    workflowConnectionRepository.deleteAllByWorkflowId(workflowId);

    List<WorkflowConnectionRequest> requestedConnections = request.connections() == null
        ? Collections.emptyList()
        : request.connections();

    validateConnectionRequests(requestedConnections, requestedNodes);

    List<WorkflowConnection> connections = requestedConnections.stream()
        .map(connectionReq -> new WorkflowConnection(
            workflowId,
            connectionReq.fromNodeId(),
            connectionReq.toNodeId()
        ))
        .toList();

    workflowConnectionRepository.saveAll(connections);
  }

  public Workflow getWorkflow(UUID workflowId) {
    return workflowRepository.findById(workflowId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 workflow입니다."));
  }

  private void validateNodeRequests(
      List<WorkflowNodeRequest> requestedNodes,
      List<WorkflowNode> existingNodes
  ) {
    Set<UUID> existingNodeIds = existingNodes.stream()
        .map(WorkflowNode::getId)
        .collect(Collectors.toSet());
    Set<UUID> requestedNodeIds = new HashSet<>();

    for (WorkflowNodeRequest nodeReq : requestedNodes) {
      if (nodeReq.id() == null) {
        throw new IllegalArgumentException("Workflow node id is required.");
      }
      if (!requestedNodeIds.add(nodeReq.id())) {
        throw new IllegalArgumentException("Duplicate workflow node id: " + nodeReq.id());
      }
      if (!nodeExecutorRegistry.contains(nodeReq.nodeKey())) {
        throw new IllegalArgumentException("No NodeExecutor registered for nodeKey: " + nodeReq.nodeKey());
      }
      if (nodeReq.position() == null) {
        throw new IllegalArgumentException("Workflow node position is required: " + nodeReq.id());
      }
      if (!existingNodeIds.contains(nodeReq.id()) && workflowNodeRepository.existsById(nodeReq.id())) {
        throw new IllegalArgumentException("Workflow node id already exists in another workflow: " + nodeReq.id());
      }
    }
  }

  private void validateConnectionRequests(
      List<WorkflowConnectionRequest> requestedConnections,
      List<WorkflowNodeRequest> requestedNodes
  ) {
    Set<UUID> nodeIds = requestedNodes.stream()
        .map(WorkflowNodeRequest::id)
        .collect(Collectors.toSet());

    for (WorkflowConnectionRequest connectionReq : requestedConnections) {
      if (connectionReq.fromNodeId() == null || connectionReq.toNodeId() == null) {
        throw new IllegalArgumentException("Workflow connection node ids are required.");
      }
      if (!nodeIds.contains(connectionReq.fromNodeId())) {
        throw new IllegalArgumentException("Connection fromNodeId is not in workflow nodes: "
            + connectionReq.fromNodeId());
      }
      if (!nodeIds.contains(connectionReq.toNodeId())) {
        throw new IllegalArgumentException("Connection toNodeId is not in workflow nodes: "
            + connectionReq.toNodeId());
      }
    }
  }

  private List<CredentialConfig> toCredentialConfigs(Map<String, UUID> credentials) {
    if (credentials == null || credentials.isEmpty()) {
      return Collections.emptyList();
    }

    return credentials.entrySet().stream()
        .map(entry -> toCredentialConfig(entry.getKey(), entry.getValue()))
        .toList();
  }

  private CredentialConfig toCredentialConfig(String key, UUID credentialId) {
    if (credentialId == null) {
      throw new IllegalArgumentException("Credential id is required for key: " + key);
    }

    CredentialType type;
    try {
      type = CredentialType.valueOf(key);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Unsupported credential type: " + key, e);
    }

    Credential credential = credentialRepository.findById(credentialId)
        .orElseThrow(() -> new IllegalArgumentException("Credential not found: " + credentialId));

    if (credential.getType() != type) {
      throw new IllegalArgumentException("Credential type mismatch for key: " + key);
    }

    return new CredentialConfig(type, key, credentialId);
  }

  private WorkflowNode createDefaultManualTriggerNode() {
    return WorkflowNode.builder()
        .id(Generators.timeBasedEpochGenerator().generate())
        .nodeKey("manual.trigger")
        .name("Manual Trigger")
        .version("1")
        .positionX(0)
        .positionY(0)
        .parameters(Collections.emptyMap())
        .credentials(Collections.emptyList())
        .build();
  }
}
