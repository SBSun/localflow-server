package com.localflow.workflow.execution.credential;

import com.localflow.workflow.domain.WorkflowNode;
import com.localflow.workflow.domain.credential.enums.CredentialType;
import com.localflow.workflow.repository.WorkflowNodeRepository;

import org.springframework.stereotype.Component;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NodeCredentialProvider {

  private final WorkflowNodeRepository workflowNodeRepository;
  private final CredentialService credentialService;

  public <T> T getForNode(UUID nodeId, CredentialType type) {
    WorkflowNode node = workflowNodeRepository.findById(nodeId)
        .orElseThrow();

    UUID credentialId = node.getCredentials().stream()
        .filter(credential -> credential.type() == type)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Credential not configured for type: " + type))
        .credentialId();

    return credentialService.resolve(credentialId);
  }
}