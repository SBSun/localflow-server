package com.localflow.workflow.domain.config;

import java.util.List;

public record WorkflowConfig(
    List<CredentialRefConfig> credentials,
    VersionConfig version
) {
}
