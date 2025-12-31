package com.localflow.workflow.application.trigger;

public interface TriggerHandler {

  TriggerType getType();

  void handle(TriggerContext context);
}
