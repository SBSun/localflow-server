package com.localflow.workflow.infra.credencial;

public record NotionCredential(
    String apiToken,
    String databaseId
) {
}
