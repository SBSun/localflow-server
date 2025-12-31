package com.localflow.workflow.api.request;

import java.util.UUID;

public record WorkflowConnectionRequest(
    UUID fromNodeId,
    UUID toNodeId
) {
}
