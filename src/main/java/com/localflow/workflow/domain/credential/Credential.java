package com.localflow.workflow.domain.credential;

import com.fasterxml.uuid.Generators;
import com.localflow.workflow.domain.credential.enums.CredentialType;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Credential {

  @Id
  private final UUID id = Generators.timeBasedEpochGenerator().generate();

  @Column(nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CredentialType type;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "encrypted_data", columnDefinition = "jsonb", nullable = false)
  private Map<String, Object> encryptedData;

  @CreatedDate
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  public Credential(String name, CredentialType type, Map<String, Object> encryptedData) {
    this.name = name;
    this.type = type;
    this.encryptedData = encryptedData;
  }
}
