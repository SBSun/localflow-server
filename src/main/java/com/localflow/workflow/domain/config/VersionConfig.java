package com.localflow.workflow.domain.config;

public record VersionConfig(
    String versionId,
    Integer versionCounter,
    Integer schemaVersion
) {
}
