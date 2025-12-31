package com.localflow.workflow.execution.credential;

import com.localflow.workflow.domain.credential.enums.CredentialType;

public interface CredentialProvider {

  <T> T get(CredentialType credentialType);
}
