package com.localflow.workflow.api.request;

import com.localflow.common.vo.Position;

import java.util.Map;
import java.util.UUID;

public record WorkflowNodeRequest(
    UUID id,
    String nodeKey,
    String name,
    String version,
    Position position,
    Map<String, Object> parameters,
    Map<String, UUID> credentials
) {
}
