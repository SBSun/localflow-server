package com.localflow.workflow.execution.credential;

import com.localflow.workflow.domain.credential.Credential;
import com.localflow.workflow.repository.CredentialRepository;

import org.springframework.stereotype.Service;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class CredentialService {

  private final CredentialRepository credentialRepository;
  private final ObjectMapper objectMapper;

  public <T> T resolve(UUID credentialId) {
    Credential credential = credentialRepository.findById(credentialId)
        .orElseThrow(() ->
            new IllegalArgumentException("Credential not found: " + credentialId)
        );

    //    validateType(config, credential);
    //
    //    Map<String, Object> decrypted =
    //        cryptoService.decrypt(credential.getEncryptedData());

    return objectMapper.convertValue(
        credential.getEncryptedData(),
        credential.getType().credentialClass()
    );
  }
}
