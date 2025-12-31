package com.localflow.workflow.execution.credential;

import com.localflow.workflow.domain.credential.enums.CredentialType;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WorkflowScopedCredentialProvider implements CredentialProvider {

  private final UUID workflowId;
  private final NodeCredentialProvider delegate;

  @Override
  public <T> T get(CredentialType type) {
    throw new UnsupportedOperationException(
        "Node context required to resolve credential"
    );
  }

  public <T> T getForNode(UUID nodeId, CredentialType type) {
    return delegate.getForNode(nodeId, type);
  }
}
