package com.localflow.workflow.api.request;

public record WorkflowCreateRequest(
    String name,
    String description
) {
}