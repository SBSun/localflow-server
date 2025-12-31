package com.localflow.workflow.repository;

import com.localflow.workflow.domain.credential.Credential;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CredentialRepository extends JpaRepository<Credential, UUID> {
}
