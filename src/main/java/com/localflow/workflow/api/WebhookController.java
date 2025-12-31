package com.localflow.workflow.api;

import com.localflow.workflow.application.trigger.TriggerContext;
import com.localflow.workflow.application.trigger.TriggerType;
import com.localflow.workflow.application.trigger.WebhookTriggerHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webhook")
public class WebhookController {

  private final WebhookTriggerHandler triggerHandler;

  @PostMapping("/{webhookId}")
  public ResponseEntity<Void> handle(
      @PathVariable String webhookId,
      @RequestBody Map<String, Object> payload
  ) {
    TriggerContext context = new TriggerContext(TriggerType.WEBHOOK, webhookId, payload);

    triggerHandler.handle(context);
    return ResponseEntity.ok().build();
  }
}
