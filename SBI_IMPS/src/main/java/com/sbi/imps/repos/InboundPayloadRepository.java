package com.sbi.imps.repos;

import com.sbi.imps.entities.InboundPayload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboundPayloadRepository extends JpaRepository<InboundPayload, String> {
}

