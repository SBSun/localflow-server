package com.localflow.workflow.application.execution;

import com.localflow.workflow.infra.executor.NodeExecutor;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NodeExecutorRegistry {

  private final Map<String, NodeExecutor> executorMap = new HashMap<>();

  public NodeExecutorRegistry(List<NodeExecutor> executors) {
    for (NodeExecutor executor : executors) {
      String key = executor.getNodeKey();

      if (executorMap.containsKey(key)) {
        throw new IllegalStateException(
            "Duplicate NodeExecutor for key: " + key
        );
      }

      executorMap.put(key, executor);
    }
  }

  public boolean contains(String nodeKey) {
    return executorMap.containsKey(nodeKey);
  }

  public NodeExecutor get(String nodeKey) {
    NodeExecutor executor = executorMap.get(nodeKey);

    if (executor == null) {
      throw new IllegalArgumentException(
          "No NodeExecutor registered for nodeKey: " + nodeKey
      );
    }

    return executor;
  }
}

