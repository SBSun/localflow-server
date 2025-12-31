package com.localflow.workflow.application.trigger;

import java.util.Map;

public record TriggerContext(
    TriggerType type,
    String triggerKey,
    Map<String, Object> payload
) {
}