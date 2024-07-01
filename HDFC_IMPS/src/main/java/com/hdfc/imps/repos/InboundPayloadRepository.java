package com.hdfc.imps.repos;

import com.hdfc.imps.entities.InboundPayload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboundPayloadRepository extends JpaRepository<InboundPayload, String> {
}

