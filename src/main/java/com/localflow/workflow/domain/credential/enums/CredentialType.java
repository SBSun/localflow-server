package com.localflow.workflow.domain.credential.enums;

import com.localflow.workflow.infra.credencial.NotionCredential;

public enum CredentialType {

  NOTION(NotionCredential.class);

  private final Class<?> credentialClass;

  CredentialType(Class<?> credentialClass) {
    this.credentialClass = credentialClass;
  }

  @SuppressWarnings("unchecked")
  public <T> Class<T> credentialClass() {
    return (Class<T>) credentialClass;
  }
}
