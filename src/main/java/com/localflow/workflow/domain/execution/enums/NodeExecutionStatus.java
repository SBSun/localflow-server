package com.localflow.workflow.domain.execution.enums;

public enum NodeExecutionStatus {
  WAITING,
  RUNNING,
  SUCCESS,
  FAILED,
  SKIPPED     // 조건 미충족 / 분기 제외
}
