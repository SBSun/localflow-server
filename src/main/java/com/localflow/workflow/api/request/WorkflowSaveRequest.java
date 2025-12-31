package com.localflow.workflow.api.request;

import java.util.List;

public record WorkflowSaveRequest(
    String name,
    String description,
    boolean active,
    List<WorkflowNodeRequest> nodes,
    List<WorkflowConnectionRequest> connections
) {
}
