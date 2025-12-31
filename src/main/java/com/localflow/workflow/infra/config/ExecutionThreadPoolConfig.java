package com.localflow.workflow.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ExecutionThreadPoolConfig {

  @Bean(name = "workflowExecutionExecutor")
  public Executor workflowExecutionExecutor() {
    return Executors.newFixedThreadPool(10);
  }
}