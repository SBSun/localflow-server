package com.localflow.workflow.domain.config;

import com.localflow.workflow.domain.credential.enums.CredentialType;

import java.util.UUID;

public record CredentialConfig(
    CredentialType type,
    String key,
    UUID credentialId
) {
}